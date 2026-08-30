namespace backend.Models;

public class Despesa
{
    public int Id { get; set; }

    public string Categoria { get; set; }

    public string Tipo { get; set; }

    public string NomeDespesa { get; set; }

    public decimal ValorOrcado { get; set; }

    public decimal ValorRealizado { get; set; }

    public int ProjetoId { get; set; }

    public Projeto? Projeto { get; set; }
}