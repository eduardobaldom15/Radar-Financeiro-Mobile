using System.ComponentModel.DataAnnotations;

namespace backend.Models;

public class LoginModel
{
	[Required]
	[EmailAddress]
	public required string Email { get; set; }

	[Required]
	[DataType(DataType.Password)]
	public required string Senha { get; set; }
}