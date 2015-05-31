package br.com.correiam.checkmeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.shamanland.fab.FloatingActionButton;

import br.com.correiam.checkmeta.dao.UsuarioDAO;
import br.com.correiam.checkmeta.dominio.Usuario;


public class HomeActivity extends ActionBarActivity implements View.OnClickListener{
    private SharedPreferences sharedPref;
    private TextView tvBoasVindasUsuario;
    private TextView tvTextoBoasVindas;
    private FloatingActionButton fab;
    private UsuarioDAO userDAO;
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvBoasVindasUsuario = (TextView) findViewById(R.id.tvBoasVindasUsuario);

        fab = (FloatingActionButton) findViewById(R.id.nova_meta);
        fab.setOnClickListener(this);

        if (getIntent().hasExtra("ID")) {
            Bundle extras = getIntent().getExtras();
            Long idUser = extras.getLong("ID");

            userDAO = new UsuarioDAO(this);
            user = userDAO.getUsuarioById(idUser);

            tvBoasVindasUsuario.setText(
                    getString(R.string.boas_vindas_usuario, user.getNome())
            );
            tvBoasVindasUsuario.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sair) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("idUser");
            editor.putBoolean("keepConnected", false);
            editor.commit();

            Intent login = new Intent(HomeActivity.this, LoginActivity.class);
            HomeActivity.this.startActivity(login);
            HomeActivity.this.finish();

            return true;
        }

        if(id == R.id.action_perfil){
            Intent perfil = new Intent(HomeActivity.this, CadastroActivity.class);
            perfil.putExtra("ID", user.getId());
            HomeActivity.this.startActivity(perfil);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.nova_meta:
                Intent meta = new Intent(HomeActivity.this, MetaActivity.class);
                HomeActivity.this.startActivity(meta);
                break;
        }
    }

}
