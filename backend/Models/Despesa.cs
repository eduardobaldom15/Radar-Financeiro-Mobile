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

    public decimal ValorOrcado { get; set; }

    public decimal ValorRealizado { get; set; }

    public int ProjetoId { get; set; }

    public Projeto? Projeto { get; set; }
}