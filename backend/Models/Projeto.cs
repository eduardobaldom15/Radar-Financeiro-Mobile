namespace backend.Models;

public class Projeto
{
    public int Id { get; set; }

    public string Nome { get; set; }

    public string Descricao { get; set; }

    public DateTime DataInicio { get; set; }

    public DateTime DataFim { get; set; }

    public string Programa { get; set; }

    // Relacionamento com Pesquisador
    public int PesquisadorId { get; set; }

    public Pesquisador? Pesquisador { get; set; }
}