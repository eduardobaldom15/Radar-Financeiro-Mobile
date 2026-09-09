using System.ComponentModel.DataAnnotations;


namespace backend.Models;

public class Pesquisador
{
    public int Id { get; set; }

    [Required]
    [MaxLength(100)]
    public required string Nome { get; set; }

    [Required]
    [EmailAddress]
    [MaxLength(100)]
    public required string Email { get; set; }

    [Required]
    [DataType(DataType.Password)]
    [MaxLength(100)]
    public required string Senha { get; set; }

    [Required]
    [MaxLength(150)]
    public required string Curso { get; set; }

    [Required]
    [MaxLength(150)]
    public string? Departamento { get; set; }

    public List<Projeto> Projetos { get; set; } = new();

    public Usuario? Usuario { get; set; }
}