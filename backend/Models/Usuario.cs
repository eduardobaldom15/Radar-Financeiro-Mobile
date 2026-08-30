namespace backend.Models;

public class Usuario
{
    public int Id { get; set; }

    public string Email { get; set; }

    public string Senha { get; set; }

    public int PesquisadorId { get; set; }

    public Pesquisador? Pesquisador { get; set; }
}