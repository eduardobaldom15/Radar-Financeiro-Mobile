using System.ComponentModel.DataAnnotations;

namespace backend.Models;

public class Projeto
{
    public int Id { get; set; }

    [Required]
    [MaxLength(200)]
    public required string Nome { get; set; }

    [MaxLength(300)]
    public string? Descricao { get; set; }

    public DateTime DataInicio { get; set; }

    public DateTime DataFim { get; set; }

    [Required]
    [MaxLength(150)]
    public required string Programa { get; set; }

    // Relacionamento com Pesquisador
    public int PesquisadorId { get; set; }

    public Pesquisador? Pesquisador { get; set; }
}