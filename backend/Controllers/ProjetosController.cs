using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using backend.Data;
using backend.Models;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;

namespace backend.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ProjetosController : ControllerBase
{
    private readonly AppDbContext _context;

    public ProjetosController(AppDbContext context)
    {
        _context = context;
    }

    [Authorize]
    [HttpGet]
    public IActionResult Get()
    {
        var userId = int.Parse(
            User.FindFirst(ClaimTypes.NameIdentifier)?.Value
        );

        var projetos = _context.Projetos
            .Where(p => p.PesquisadorId == userId)
            .ToList();


        return Ok(projetos);
    }

    [Authorize]
    [HttpPost]
    public IActionResult Post(Projeto projeto)
    {
        var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

        projeto.PesquisadorId = userId;

        _context.Projetos.Add(projeto);
        _context.SaveChanges();

        return CreatedAtAction(nameof(Get), new { id = projeto.Id }, projeto);
    }

    [Authorize]
    [HttpPut("{id}")]
    public IActionResult Put(int id, Projeto projetoAtualizado)
    {
        var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

        var projeto = _context.Projetos
            .FirstOrDefault(p => p.Id == id && p.PesquisadorId == userId);

        if (projeto == null)
            return NotFound();

        projeto.Nome = projetoAtualizado.Nome;
        projeto.DataInicio = projetoAtualizado.DataInicio;
        projeto.DataFim = projetoAtualizado.DataFim;
        projeto.Programa = projetoAtualizado.Programa;
        projeto.Descricao = projetoAtualizado.Descricao;

        _context.SaveChanges();

        return Ok(projeto);
    }

    [Authorize]
    [HttpDelete("{id}")]
    public IActionResult Delete(int id)
    {
        var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

        var projeto = _context.Projetos
            .FirstOrDefault(p => p.Id == id && p.PesquisadorId == userId);

        if (projeto == null)
            return NotFound();

        _context.Projetos.Remove(projeto);
        _context.SaveChanges();

        return NoContent();
    }

    [Authorize]
    [HttpGet("{id}/dashboard")]
    public IActionResult Dashboard(int id)
    {
        var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

        var projeto = _context.Projetos
            .FirstOrDefault(p => p.Id == id && p.PesquisadorId == userId);

        if (projeto == null)
            return NotFound();

        var receitaTotal = _context.Receitas
            .Where(r => r.ProjetoId == id)
            .Sum(r => r.Valor);

        var despesaOrcada = _context.Despesas
            .Where(d => d.ProjetoId == id)
            .Sum(d => d.ValorOrcado);

        var despesaRealizada = _context.Despesas
            .Where(d => d.ProjetoId == id)
            .Sum(d => d.ValorRealizado);

        var valorDisponivel = receitaTotal - despesaRealizada;
        var economia = despesaOrcada - despesaRealizada;

        return Ok(new
        {
            totalReceitas = receitaTotal,
            totalDespesas = despesaRealizada,
            valorDisponivel,
            economia,
            despesaOrcada,
            projeto = projeto.Nome
        });
    }
}