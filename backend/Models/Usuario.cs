using System.ComponentModel.DataAnnotations;

namespace backend.Models;

public class Usuario
{
    public int Id { get; set; }

    [Required]
    [EmailAddress]
    [MaxLength(100)]
    public required string Email { get; set; }

    [Required]
    [DataType(DataType.Password)]
    [MaxLength(100)]
    public required string Senha { get; set; }

    public int PesquisadorId { get; set; }

    public Pesquisador? Pesquisador { get; set; }
}