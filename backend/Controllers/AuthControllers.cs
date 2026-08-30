using Microsoft.AspNetCore.Mvc;
using backend.Data;
using backend.Models;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace backend.Controllers;

[ApiController]
[Route("api/[controller]")]
public class AuthController : ControllerBase
{
    private readonly AppDbContext _context;
    private readonly IConfiguration _configuration;

    public AuthController(
        AppDbContext context,
        IConfiguration configuration)
    {
        _context = context;
        _configuration = configuration;
    }

    [HttpPost("login")]
    public IActionResult Login(LoginModel login)
    {
        var pesquisador = _context.Pesquisadores
            .FirstOrDefault(p =>
                p.Email.Trim().ToLower() == login.Email.Trim().ToLower()
            );

        if (pesquisador == null || pesquisador.Senha != login.Senha)
            return Unauthorized("Usuário ou senha inválidos");

        var claims = new[]
        {
            new Claim(
                ClaimTypes.NameIdentifier,
                pesquisador.Id.ToString()
            ),

            new Claim(
                ClaimTypes.Name,
                pesquisador.Nome
            ),

            new Claim(
                ClaimTypes.Email,
                pesquisador.Email
            )
        };

        var key = new SymmetricSecurityKey(
            Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]!)
        );

        var credentials = new SigningCredentials(
            key,
            SecurityAlgorithms.HmacSha256
        );

        var token = new JwtSecurityToken(
            issuer: _configuration["Jwt:Issuer"],
            audience: _configuration["Jwt:Audience"],
            claims: claims,
            expires: DateTime.Now.AddHours(2),
            signingCredentials: credentials
        );

        var tokenString = new JwtSecurityTokenHandler()
            .WriteToken(token);

        return Ok(new
        {
            token = tokenString
        });
    }
}