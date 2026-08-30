namespace backend.Models;

public class Pesquisador
{
    public int Id { get; set; }

    public string Nome { get; set; }

    public string Email { get; set; }

    public string Senha { get; set; }

    public string Curso { get; set; }

    public string Departamento { get; set; }

    public List<Projeto> Projetos { get; set; } = new();

    public Usuario? Usuario { get; set; }
}