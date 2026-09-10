using System.ComponentModel.DataAnnotations;

namespace backend.Models;

public class Despesa
{
    public int Id { get; set; }
    
    [Required]
    [MaxLength(100)]
    public required string Categoria { get; set; }

    [Required]
    [MaxLength(100)]
    public required string Tipo { get; set; }

    [Required]
    [MaxLength(250)]
    public required string NomeDespesa { get; set; }

    [MaxLength(250)]
    public string? Descricao { get; set; }

    [Required]
    [Range(0, double.MaxValue, ErrorMessage = "O valor orçado não pode ser negativo.")]
    public decimal ValorUnitario { get; set; }

    [Required]
    [Range(0, double.MaxValue, ErrorMessage = "O valor realizado não pode ser negativo.")]
    public decimal Quantidade { get; set; }

    [Range(0, double.MaxValue, ErrorMessage = "O valor orçado não pode ser negativo.")]
    public decimal ValorOrcado { get; set; }

    [Range(0, double.MaxValue, ErrorMessage = "O valor realizado não pode ser negativo.")]
    public decimal ValorRealizado { get; set; }

    public int ProjetoId { get; set; }

    public Projeto? Projeto { get; set; }
}