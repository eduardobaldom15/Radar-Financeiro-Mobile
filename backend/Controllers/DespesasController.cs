using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using backend.Data;
using backend.Models;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;

namespace backend.Controllers;

[ApiController]
[Route("api/[controller]")]
public class DespesasController : ControllerBase
{
	private readonly AppDbContext _context;

	public DespesasController(AppDbContext context)
	{
		_context = context;
	}

	// GET por projeto (seguro)
	[Authorize]
	[HttpGet("projeto/{id}")]
	public IActionResult GetByProjeto(int id)
	{
		var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

		var despesas = _context.Despesas
			.Include(d => d.Projeto)
			.Where(d => d.ProjetoId == id && d.Projeto.PesquisadorId == userId)
			.ToList();

		return Ok(despesas);
	}

	// POST com validação de segurança
	[Authorize]
	[HttpPost]
	public IActionResult Post(Despesa despesa)
	{
		var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

		var projetoValido = _context.Projetos
			.FirstOrDefault(p => p.Id == despesa.ProjetoId && p.PesquisadorId == userId);

		if (projetoValido == null)
			return BadRequest("Projeto inválido");

		_context.Despesas.Add(despesa);
		_context.SaveChanges();

		return Ok(despesa);
	}

	// PUT seguro
	[Authorize]
	[HttpPut("{id}")]
	public IActionResult Put(int id, Despesa despesaAtualizada)
	{
		var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

		var despesa = _context.Despesas
			.Include(d => d.Projeto)
			.FirstOrDefault(d => d.Id == id && d.Projeto.PesquisadorId == userId);

		if (despesa == null)
			return NotFound();

		despesa.Categoria = despesaAtualizada.Categoria;
		despesa.Tipo = despesaAtualizada.Tipo;
		despesa.NomeDespesa = despesaAtualizada.NomeDespesa;
		despesa.ValorOrcado = despesaAtualizada.ValorOrcado;
		despesa.ValorRealizado = despesaAtualizada.ValorRealizado;
		despesa.ProjetoId = despesaAtualizada.ProjetoId;

		_context.SaveChanges();

		return Ok(despesa);
	}

	// DELETE seguro
	[Authorize]
	[HttpDelete("{id}")]
	public IActionResult Delete(int id)
	{
		var userId = int.Parse(User.FindFirst(ClaimTypes.NameIdentifier)!.Value);

		var despesa = _context.Despesas
			.Include(d => d.Projeto)
			.FirstOrDefault(d => d.Id == id && d.Projeto.PesquisadorId == userId);

		if (despesa == null)
			return NotFound();

		_context.Despesas.Remove(despesa);
		_context.SaveChanges();

		return NoContent();
	}
}