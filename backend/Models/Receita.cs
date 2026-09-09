using System.ComponentModel.DataAnnotations;

namespace backend.Models;

public class Receita
{
	public int Id { get; set; }

    [Required]
    [MaxLength(150)]
	public required string Tipo { get; set; }

    [Required]
    [MaxLength(150)]
	public required string Origem { get; set; }

	public DateTime DataEntrada { get; set; }

	public decimal Valor { get; set; }

	// Relacionamento com Projeto
	public int ProjetoId { get; set; }

	public Projeto? Projeto { get; set; }
}