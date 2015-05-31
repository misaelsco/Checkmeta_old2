package br.com.correiam.checkmeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.correiam.checkmeta.dao.UsuarioDAO;
import br.com.correiam.checkmeta.dominio.Usuario;


public class CadastroActivity extends ActionBarActivity implements View.OnClickListener  {

    private EditText etNome;
    private EditText etEmail;
    private EditText etSenha;
    private EditText etConfirmacaoSenha;
    private TextView tvSenha;
    private TextView tvConfirmacaoSenha;
    private Button btnCadastrar;
    private SharedPreferences sharedPref;

    private Usuario usu;
    private UsuarioDAO dao;
    private Long idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dao = new UsuarioDAO(this);

        etNome = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etSenha = (EditText) findViewById(R.id.etPassword);
        etConfirmacaoSenha = (EditText) findViewById(R.id.etConfirmPassword);
        tvSenha = (TextView) findViewById(R.id.tvSenha);
        tvConfirmacaoSenha = (TextView) findViewById(R.id.tvConfirmacaoSenha);

        btnCadastrar = (Button) this.findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(this);

        if(getIntent().hasExtra("ID")){
            Bundle extras = getIntent().getExtras();
            idUser = extras.getLong("ID");
            usu = new Usuario();
            dao = new UsuarioDAO(this);
            usu = dao.getUsuarioById(idUser);

            etNome.setText(usu.getNome());
            etEmail.setText(usu.getEmail());
            if(usu.getIsFacebookUser()){
                tvSenha.setVisibility(View.GONE);
                etSenha.setVisibility(View.GONE);
                tvConfirmacaoSenha.setVisibility(View.GONE);
                etConfirmacaoSenha.setVisibility(View.GONE);
                btnCadastrar.setVisibility(View.GONE);
                etNome.setEnabled(false);
                etEmail.setEnabled(false);
            }
            else{
                tvSenha.setText("Nova senha");
                tvConfirmacaoSenha.setText("Confirme a nova senha");
                btnCadastrar.setText("SALVAR");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnCadastrar:
                salvar();
                break;
        }
    }

    public void salvar(){
        if(validaCampos()){
            Usuario user = new Usuario();
            user.setNome(etNome.getText().toString());
            user.setEmail(etEmail.getText().toString());
            user.setSenha(etSenha.getText().toString());
            user.setIsFacebookUser(false);

            if(usu != null && !user.equals(usu)){
                dao.update(user);
            }
            else
            {
                Long insertedId = dao.insert(user);

                if (insertedId > -1)
                {
                    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putLong("idUser", insertedId);
                    editor.putBoolean("keepConnected", true);
                    editor.commit();

                    Toast.makeText(CadastroActivity.this, "Usuário registrado com sucesso", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(CadastroActivity.this, HomeActivity.class);
                    intent.putExtra("ID", insertedId);
                    CadastroActivity.this.startActivity(intent);
                    this.finish();
                }else {
                    Toast.makeText(CadastroActivity.this, "Falha ao registrar usuário", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean validaCampos() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String confSenha = etConfirmacaoSenha.getText().toString().trim();
        return (!isEmptyFields(nome, email, senha, confSenha) && isValidContent(nome, email, senha, confSenha));
    }

    private boolean isEmptyFields( String nome, String email, String senha, String confSenha) {
        if (TextUtils.isEmpty(nome)) {
            etNome.requestFocus();
            etNome.setError("Campo 'Nome' é obrigatório");
            return true;
        }
        else if (TextUtils.isEmpty(email)) {
            etEmail.requestFocus();
            etEmail.setError("Campo 'E-mail' é obrigatório");
            return true;
        }
        else if(idUser == null) {
            if (TextUtils.isEmpty(senha)) {
                etSenha.requestFocus();
                etSenha.setError("Campo 'Senha' é obrigatório");
                return true;
            } else if (TextUtils.isEmpty(confSenha)) {
                etConfirmacaoSenha.requestFocus();
                etConfirmacaoSenha.setError("Campo 'Confirme sua senha' é obrigatório");
                return true;
            }
        }
        return false;
    }

    private boolean isValidContent(String nome, String email, String senha, String confSenha) {

        if (!(nome.length() >= 3)) {
            etNome.requestFocus();
            etNome.setError("O Nome deve conter no mínimo 3 caracteres");
            return false;
        }
        else if (!(email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && email.length() >= 6)) {
            etEmail.requestFocus();
            etEmail.setError("Digite um e-mail válido");
            return false;
        }
        else if (senha.length() > 0 || confSenha.length() > 0) {
            if (!(senha.length() >= 6 && senha.length() <= 10)) {
                etSenha.requestFocus();
                etSenha.setError("A senha deve conter entre 6 e 10 caracteres");
                return false;
            } else if (!(confSenha.equals(senha))) {
                etConfirmacaoSenha.requestFocus();
                etConfirmacaoSenha.setError("Senhas não conferem");
                return false;
            }
        }
        else if (usu != null && usu.getId() != dao.ehDuplicado(email)) {
            etEmail.requestFocus();
            etEmail.setError("Este e-mail já está cadastrado!");
            return false;
        }
        else if (dao.ehDuplicado(email) > -1) {
            etEmail.requestFocus();
            etEmail.setError("Este e-mail já está cadastrado!");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
