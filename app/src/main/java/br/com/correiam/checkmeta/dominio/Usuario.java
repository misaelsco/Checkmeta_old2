package br.com.correiam.checkmeta.dominio;

/**
 * Created by Misael Correia on 19/05/15.
 * misaelsco@gmail.com
 */
public class Usuario {

    private long id;
    private String nome;
    private String email;
    private String senha;
    private boolean isFacebookUser;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean getIsFacebookUser() {
        return isFacebookUser;
    }

    public void setIsFacebookUser(boolean ehUsuarioFacebook) {
        this.isFacebookUser = ehUsuarioFacebook;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;

        Usuario usuario = (Usuario) o;

        if (isFacebookUser != usuario.isFacebookUser) return false;
        if (id != usuario.id) return false;
        if (email != null ? !email.equals(usuario.email) : usuario.email != null) return false;
        if (nome != null ? !nome.equals(usuario.nome) : usuario.nome != null) return false;
        if (senha != null ? !senha.equals(usuario.senha) : usuario.senha != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (senha != null ? senha.hashCode() : 0);
        result = 31 * result + (isFacebookUser ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", isFacebookUser=" + isFacebookUser +
                '}';
    }
}
