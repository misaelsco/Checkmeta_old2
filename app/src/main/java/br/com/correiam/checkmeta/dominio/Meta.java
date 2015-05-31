package br.com.correiam.checkmeta.dominio;

/**
 * Created by Misael Correia on 27/05/2015.
 * misaelsco@gmail.com
 */
public class Meta {
    private Long id;
    private String nome;
    private String descricao;
    private String dueDate;
    private String status;
    private String actualDate;
    private Long idUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActualDate() {
        return actualDate;
    }

    public void setActualDate(String actualDate) {
        this.actualDate = actualDate;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meta meta = (Meta) o;

        if (actualDate != null ? !actualDate.equals(meta.actualDate) : meta.actualDate != null)
            return false;
        if (descricao != null ? !descricao.equals(meta.descricao) : meta.descricao != null)
            return false;
        if (!dueDate.equals(meta.dueDate)) return false;
        if (id != null ? !id.equals(meta.id) : meta.id != null) return false;
        if (!idUser.equals(meta.idUser)) return false;
        if (!nome.equals(meta.nome)) return false;
        if (status != null ? !status.equals(meta.status) : meta.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + nome.hashCode();
        result = 31 * result + (descricao != null ? descricao.hashCode() : 0);
        result = 31 * result + dueDate.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (actualDate != null ? actualDate.hashCode() : 0);
        result = 31 * result + idUser.hashCode();
        return result;
    }
}
