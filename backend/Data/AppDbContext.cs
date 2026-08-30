using Microsoft.EntityFrameworkCore;
using backend.Models;

namespace backend.Data;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options)
        : base(options)
    {
    }

    public DbSet<Pesquisador> Pesquisadores { get; set; }

    public DbSet<Projeto> Projetos { get; set; }

    public DbSet<Receita> Receitas { get; set; }

    public DbSet<Despesa> Despesas { get; set; }

    public DbSet<Usuario> Usuarios { get; set; }
}