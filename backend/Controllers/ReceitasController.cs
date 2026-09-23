using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using backend.Data;
using backend.Models;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;

namespace backend.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ReceitasController : ControllerBase
{
    private readonly AppDbContext _context;

    public ReceitasController(AppDbContext context)
    {
        _context = context;
    }

    [Authorize]
    [HttpGet("projeto/{id}")]
    public IActionResult GetByProjeto(int id)
    {
        try
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

            var receitas = _context.Receitas
                .Include(r => r.Projeto)
                .Where(r => r.ProjetoId == id && r.Projeto!.PesquisadorId == userId)
                .ToList();

            return Ok(receitas);
        }
        catch (Exception)
        {
            return StatusCode(500, $"Erro ao buscar receitas.");
        }
    }

    [Authorize]
    [HttpPost]
    public IActionResult Post(Receita receita)
    {
        try
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

            // segurança: garante que não criem em projeto de outro usuário
            var projetoValido = _context.Projetos
                .FirstOrDefault(p => p.Id == receita.ProjetoId && p.PesquisadorId == userId);

            if (projetoValido == null)
                return BadRequest("Projeto inválido");

            receita.DataEntrada = DateTime.Now;

            _context.Receitas.Add(receita);
            _context.SaveChanges();

            return CreatedAtAction(nameof(GetByProjeto), new { id = receita.ProjetoId }, receita);
        }
        catch (Exception)
        {
            return StatusCode(500, $"Erro ao criar receita.");
        }
    }

    [Authorize]
    [HttpPut("{id}")]
    public IActionResult Put(int id, Receita receitaAtualizada)
    {
        try
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

            var receita = _context.Receitas
                .Include(r => r.Projeto)
                .FirstOrDefault(r => r.Id == id && r.Projeto!.PesquisadorId == userId);

            if (receita == null)
                return NotFound();

            receita.Tipo = receitaAtualizada.Tipo;
            receita.Origem = receitaAtualizada.Origem;
            receita.Valor = receitaAtualizada.Valor;
            receita.DataEntrada = receitaAtualizada.DataEntrada;
            receita.ProjetoId = receitaAtualizada.ProjetoId;

            _context.SaveChanges();

            return Ok(receita);
        }
        catch (Exception)
        {
            return StatusCode(500, $"Erro ao atualizar receita.");
        }
    }

    [Authorize]
    [HttpDelete("{id}")]
    public IActionResult Delete(int id)
    {
        try
        {
            var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

            var receita = _context.Receitas
                .Include(r => r.Projeto)
                .FirstOrDefault(r => r.Id == id && r.Projeto!.PesquisadorId == userId);

            if (receita == null)
                return NotFound();

            _context.Receitas.Remove(receita);
            _context.SaveChanges();

            return NoContent();
        }
        catch (Exception)
        {
            return StatusCode(500, $"Erro ao deletar receita.");
        }
    }
}