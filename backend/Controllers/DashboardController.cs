using Microsoft.AspNetCore.Mvc;
using backend.Data;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;

namespace backend.Controllers;

[ApiController]
[Route("api/[controller]")]
public class DashboardController : ControllerBase
{
    private readonly AppDbContext _context;

    public DashboardController(AppDbContext context)
    {
        _context = context;
    }

    [HttpGet("pesquisador/{id}")]
    public IActionResult GetDashboard(int id)
    {
        var projetos = _context.Projetos
            .Where(p => p.PesquisadorId == id)
            .ToList();

        var projetoIds = projetos.Select(p => p.Id).ToList();

        var totalReceitas = _context.Receitas
            .Where(r => projetoIds.Contains(r.ProjetoId))
            .Sum(r => r.Valor);

        var despesaOrcada = _context.Despesas
            .Where(d => projetoIds.Contains(d.ProjetoId))
            .Sum(d => d.ValorOrcado);

        var despesaRealizada = _context.Despesas
            .Where(d => projetoIds.Contains(d.ProjetoId))
            .Sum(d => d.ValorRealizado);

        var valorDisponivel = totalReceitas - despesaRealizada;

        var economia = despesaOrcada - despesaRealizada;

        return Ok(new
        {
            quantidadeProjetos = projetos.Count,
            totalReceitas,
            totalDespesas = despesaRealizada,
            economia,
            valorDisponivel
        });
    }

    [Authorize]
    [HttpGet("me")]
    public IActionResult GetMeuDashboard()
    {
        var userId = int.Parse(
            User.FindFirst(ClaimTypes.NameIdentifier)!.Value
        );

        var projetos = _context.Projetos
            .Where(p => p.PesquisadorId == userId)
            .ToList();

        var projetoIds = projetos.Select(p => p.Id).ToList();

        var totalReceitas = _context.Receitas
            .Where(r => projetoIds.Contains(r.ProjetoId))
            .Sum(r => r.Valor);

        var despesaOrcada = _context.Despesas
            .Where(d => projetoIds.Contains(d.ProjetoId))
            .Sum(d => d.ValorOrcado);

        var despesaRealizada = _context.Despesas
            .Where(d => projetoIds.Contains(d.ProjetoId))
            .Sum(d => d.ValorRealizado);

        var valorDisponivel = totalReceitas - despesaRealizada;

        var economia = despesaOrcada - despesaRealizada;

        return Ok(new
        {
            quantidadeProjetos = projetos.Count,
            totalReceitas,
            totalDespesas = despesaRealizada,
            economia,
            valorDisponivel
        });
    }
}