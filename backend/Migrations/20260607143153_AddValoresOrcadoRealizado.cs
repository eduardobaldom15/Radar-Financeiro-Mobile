using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace backend.Migrations
{
    /// <inheritdoc />
    public partial class AddValoresOrcadoRealizado : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Quantidade",
                table: "Despesas");

            migrationBuilder.RenameColumn(
                name: "ValorUnitario",
                table: "Despesas",
                newName: "ValorRealizado");

            migrationBuilder.RenameColumn(
                name: "ValorTotal",
                table: "Despesas",
                newName: "ValorOrcado");
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.RenameColumn(
                name: "ValorRealizado",
                table: "Despesas",
                newName: "ValorUnitario");

            migrationBuilder.RenameColumn(
                name: "ValorOrcado",
                table: "Despesas",
                newName: "ValorTotal");

            migrationBuilder.AddColumn<int>(
                name: "Quantidade",
                table: "Despesas",
                type: "int",
                nullable: false,
                defaultValue: 0);
        }
    }
}
