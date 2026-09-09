using Microsoft.AspNetCore.Mvc;
using backend.Data;
using backend.Models;
using Microsoft.AspNetCore.Authorization;
using System.Security.Claims;

namespace backend.Controllers;

[ApiController]
[Route("api/[controller]")]
public class PesquisadoresController : ControllerBase
{
    private readonly AppDbContext _context;

    public PesquisadoresController(AppDbContext context)
    {
        _context = context;
    }

    [Authorize]
    [HttpGet]
    public IActionResult Get()
    {
        try
        {
            var pesquisadores = _context.Pesquisadores.ToList();

            return Ok(pesquisadores);
        }
        catch (Exception)
        {
            return StatusCode(500, $"Erro ao buscar pesquisadores.");
        }
    }

    [HttpPost]
    public IActionResult Post(Pesquisador pesquisador)
    {
        try
        {
            _context.Pesquisadores.Add(pesquisador);

            _context.SaveChanges();

            return CreatedAtAction(nameof(Get), new { id = pesquisador.Id }, pesquisador);
        }
        catch (Exception)
        {
            return StatusCode(500, $"Erro ao criar pesquisador.");
        }
    }

    [HttpPut("{id}")]
    public IActionResult Put(int id, Pesquisador pesquisadorAtualizado)
    {
        try
        {
            var pesquisador = _context.Pesquisadores.Find(id);

            if (pesquisador == null)
            {
                return NotFound();
            }

            pesquisador.Nome = pesquisadorAtualizado.Nome;
            pesquisador.Email = pesquisadorAtualizado.Email;
            pesquisador.Curso = pesquisadorAtualizado.Curso;
            pesquisador.Departamento = pesquisadorAtualizado.Departamento;

            _context.SaveChanges();

            return Ok(pesquisador);
        }
        catch (Exception)
        {
            return StatusCode(500, $"Erro ao atualizar pesquisador.");
        }
    }

    [HttpDelete("{id}")]
    public IActionResult Delete(int id)
    {
        try
        {
            var pesquisador = _context.Pesquisadores.Find(id);

            if (pesquisador == null)
            {
                return NotFound();
            }

            _context.Pesquisadores.Remove(pesquisador);

            _context.SaveChanges();

            return NoContent();
        }
        catch (Exception)
        {
            return StatusCode(500, $"Erro ao deletar pesquisador.");
        }
    }

    [HttpGet("{id}/projetos")]
    public IActionResult GetProjetos(int id)
    {
        try
        {
            var projetos = _context.Projetos
                .Where(p => p.PesquisadorId == id)
                .ToList();

            return Ok(projetos);
        }
        catch (Exception)
        {
            return StatusCode(500, $"Erro ao buscar projetos.");
        }
    }

    [Authorize]
    [HttpGet("me/projetos")]
    public IActionResult GetMeusProjetos()
    {
        try
        {
            var userId = int.Parse(
                User.FindFirst(ClaimTypes.NameIdentifier)!.Value
            );

            var projetos = _context.Projetos
                .Where(p => p.PesquisadorId == userId)
                .ToList();

            return Ok(projetos);
        }
        catch (Exception)
        {
            return StatusCode(500, $"Erro ao buscar projetos do pesquisador autenticado.");
        }
    }
}