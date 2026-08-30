namespace backend.Models;

public class Receita
{
	public int Id { get; set; }

	public string Tipo { get; set; }

	public string Origem { get; set; }

	public DateTime DataEntrada { get; set; }

	public decimal Valor { get; set; }

	// Relacionamento com Projeto
	public int ProjetoId { get; set; }

	public Projeto? Projeto { get; set; }
}