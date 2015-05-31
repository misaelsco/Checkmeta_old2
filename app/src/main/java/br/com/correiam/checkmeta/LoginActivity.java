package br.com.correiam.checkmeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.correiam.checkmeta.dao.UsuarioDAO;
import br.com.correiam.checkmeta.dominio.Usuario;

public class LoginActivity extends Activity implements View.OnClickListener{

    private TextView tvCadastrar;
    private Context context;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);


        context = this.getBaseContext();

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile, email");
        loginButton.registerCallback(callbackManager, mCallBack);

        tvCadastrar = (TextView) findViewById(R.id.tvCadastroLink);
        tvCadastrar.setOnClickListener(this);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        tvCadastrar = (TextView) findViewById(R.id.tvCadastroLink);
        tvCadastrar.setOnClickListener(this);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        logoff();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.tvCadastroLink:
                onClickCadastrar();
                break;
            case R.id.btnLogin:
                doLogin();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            GraphRequest.newMeRequest(
                    loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject obj, GraphResponse response) {
                            if (response.getError() != null) {
                                Log.e("ERROR", " Facebook error: " + response.getError().toString());
                            } else {
                                Usuario usu = new Usuario();
                                usu.setNome(obj.optString("name"));
                                usu.setEmail(obj.optString("email"));
                                usu.setIsFacebookUser(true);

                                salvarUsuarioFacebook(usu);

                                Log.d("DEBUG", "JSON: " + obj.toString() + ", " + usu.toString());
                            }
                        }
                    }).executeAsync();

        }

        @Override
        public void onCancel() {
            Toast.makeText(context, "Cancelou", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(context, "Erro", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void salvarUsuarioFacebook(Usuario usu){
        Log.d("DEBUG", "Entrei no SalvarUsuarioFacebook");
        if(usu.getNome() != "" && usu.getEmail() != "" && usu.getIsFacebookUser() != false){
            UsuarioDAO dao = new UsuarioDAO(this);
            Long idUser = dao.ehDuplicado(usu.getEmail());
            if(idUser == -1){
                Long insertedAt = dao.insert(usu);
                if (insertedAt > -1) {
                    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putLong("idUser", insertedAt);
                    editor.putBoolean("keepConnected", true);
                    editor.commit();


                    idUser = sharedPref.getLong("idUser", -1);
                    Log.d("SharedPreference", "idUser Login: " + idUser);
                    Boolean keepConnected = sharedPref.getBoolean("keepConnected", false);
                    Log.d("SharedPreference", "keepConnected Login: " + keepConnected);


                    Toast.makeText(this, "Bem vindo " + usu.getNome(), Toast.LENGTH_LONG).show();
                    goToHome(insertedAt);
                } else {
                    Log.e("ERROR", "Erro ao cadastrar usuário com facebook: " + usu.toString());
                    Toast.makeText(this, "Erro!", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                goToHome(idUser);
            }
        }
        else
        {
            //TODO Logar o motivo porque não vieram dados
            Log.e("ERROR", "Usuario facebook incompleto: " + usu.toString());
            Toast.makeText(this, "Erro. Favor tente novamente!", Toast.LENGTH_SHORT);
        }
    }


    //Chama a tela de cadastro quando clicado no TextView com a frase de cadastro
    public void onClickCadastrar()
    {
        Intent iCadastro  = new Intent(LoginActivity.this, CadastroActivity.class);
        LoginActivity.this.startActivity(iCadastro);
    }

    public void doLogin()
    {
        if(etEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z]+\\.+[a-zA-Z]+") && etPassword.length() >= 6) {
            UsuarioDAO dao = new UsuarioDAO(this);
            Long idUser = dao.validaCredenciais(etEmail.getText().toString(), etPassword.getText().toString());
            if(idUser > 0) {
                Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                home.putExtra("ID", idUser);
                LoginActivity.this.startActivity(home);
                this.finish();
            }else{
                Toast.makeText(this, "E-mail e/ou Senha inválidos", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(this, "E-mail e/ou Senha inválidos", Toast.LENGTH_LONG).show();
        }
    }

    public void goToHome(Long idUsuario)
    {
        Intent intent  = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("ID", idUsuario);
        LoginActivity.this.startActivity(intent);
        this.finish();
    }

    private void logoff()
    {
        try {
            LoginManager.getInstance().logOut();
        }
        catch (Exception e){
            Log.d("ERROR", "Erro ao fazer logoff com facebook");
        }
    }

    public void generateKeyHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "br.com.correiam.checkmeta",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash", "Falhou: " + e.toString());

        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash", "Falhou: " + e.toString());
        }
    }
}