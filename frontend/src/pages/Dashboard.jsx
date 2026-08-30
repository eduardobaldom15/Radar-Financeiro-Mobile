import { useEffect, useState } from "react";
import axios from "axios";
import "./../styles/dashboard.css";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const [dados, setDados] = useState(null);
  const [projetos, setProjetos] = useState([]);
  const [projetoSelecionado, setProjetoSelecionado] = useState(null);
  const [receitas, setReceitas] = useState([]);
  const [despesas, setDespesas] = useState([]);
  const [novaReceita, setNovaReceita] = useState({
  origem: "",
  tipo: "",
  valor: ""
});
  const [novaDespesa, setNovaDespesa] = useState({
  nomeDespesa: "",
  categoria: "",
  valorOrcado: "",
  valorRealizado: ""
});
   const [novoProjeto, setNovoProjeto] = useState({
   nome: "",
   descricao: "",
   programa: "",
   dataInicio: "",
   dataFim: ""
});
    const { logout } = useAuth();
    const navigate = useNavigate();

async function carregarDados() {
  try {
    const token = localStorage.getItem("token");

      const dashboardUrl = projetoSelecionado
          ? `http://localhost:5279/api/Projetos/${projetoSelecionado}/dashboard`
          : `http://localhost:5279/api/Dashboard/me`;

    const dashboardResponse = await axios.get(dashboardUrl, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    setDados(dashboardResponse.data);

    const projetosResponse = await axios.get(
      "http://localhost:5279/api/Pesquisadores/me/projetos",
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );

    setProjetos(projetosResponse.data);

    if (projetoSelecionado) {
      const receitasResponse = await axios.get(
        `http://localhost:5279/api/Receitas/projeto/${projetoSelecionado}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      setReceitas(receitasResponse.data);

      const despesasResponse = await axios.get(
        `http://localhost:5279/api/Despesas/projeto/${projetoSelecionado}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      setDespesas(despesasResponse.data);
    } else {
      setReceitas([]);
      setDespesas([]);
    }

  } catch (err) {
    console.log("Erro ao carregar dados", err);
  }
}

useEffect(() => {
  carregarDados();
}, [projetoSelecionado]);

  if (!dados) return <p>Carregando...</p>;

async function adicionarReceita() {
  try {
    const token = localStorage.getItem("token");

    await axios.post(
      "http://localhost:5279/api/Receitas",
      {
        origem: novaReceita.origem,
        tipo: novaReceita.tipo,
        valor: Number(novaReceita.valor),
        dataEntrada: new Date(),
        projetoId: Number(projetoSelecionado)
      },
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );

    await carregarDados();

      setNovaReceita({
      origem: "",
      tipo: "",
      valor: ""
    });

  } catch (err) {
    console.log("Erro ao criar receita", err);
  }
}
async function adicionarDespesa() {
  try {
    const token = localStorage.getItem("token");

    await axios.post(
      "http://localhost:5279/api/Despesas",
      {
        nomeDespesa: novaDespesa.nomeDespesa,
        categoria: novaDespesa.categoria,
        tipo: "Despesa",
        valorOrcado: Number(novaDespesa.valorOrcado),
        valorRealizado: Number(novaDespesa.valorRealizado),
        projetoId: Number(projetoSelecionado)
      },
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );

    await carregarDados();

    setNovaDespesa({
      nomeDespesa: "",
      categoria: "",
      valorOrcado: "",
      valorRealizado: ""
    });

  } catch (err) {
    console.log("Erro ao criar despesa", err);
  }
}
    async function excluirReceita(id) {

    if (!window.confirm("Deseja excluir esta despesa?"))
    return;

      try {
        const token = localStorage.getItem("token");

        await axios.delete(
          `http://localhost:5279/api/Receitas/${id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`
            }
          }
        );

        await carregarDados();

      } catch (err) {
        console.log("Erro ao excluir receita", err);
      }
    }

    async function excluirDespesa(id) {

  if (!window.confirm("Deseja excluir esta despesa?"))
    return;

  try {
    const token = localStorage.getItem("token");

    await axios.delete(
      `http://localhost:5279/api/Despesas/${id}`,
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );

    await carregarDados();

  } catch (err) {
    console.log("Erro ao excluir despesa", err);
  }
}

    async function adicionarProjeto() {
        try {
            const token = localStorage.getItem("token");

            await axios.post(
                "http://localhost:5279/api/Projetos",
                {
                    nome: novoProjeto.nome,
                    descricao: novoProjeto.descricao,
                    programa: novoProjeto.programa,
                    dataInicio: novoProjeto.dataInicio,
                    dataFim: novoProjeto.dataFim
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            setNovoProjeto({
                nome: "",
                descricao: "",
                programa: "",
                dataInicio: "",
                dataFim: ""
            });

            await carregarDados();

        } catch (err) {
            console.log("Erro ao criar projeto", err);
        }
    }

    function fazerLogout() {
        logout();
        navigate("/");
    }

return (
  <>
    <div className="dashboard">
            <div className="header">
                <div>
                    <h1>Radar Financeiro</h1>
                    <p>Gerenciamento de projetos de pesquisa</p>
                </div>

                <button
                    className="indicadores-btn"
                    onClick={() => navigate("/graficos")}
                >
                    Indicadores
                </button>

                <button
                    className="logout-btn"
                    onClick={logout}
                >
                    Sair
                </button>
            </div>
   
            <div className="card">
            <h2>Novo Projeto</h2>

            <input
                placeholder="Nome"
                value={novoProjeto.nome}
                onChange={(e) =>
                    setNovoProjeto({
                        ...novoProjeto,
                        nome: e.target.value
                    })
                }
            />

            <input
                placeholder="Descrição"
                value={novoProjeto.descricao}
                onChange={(e) =>
                    setNovoProjeto({
                        ...novoProjeto,
                        descricao: e.target.value
                    })
                }
            />

            <input
                placeholder="Programa"
                value={novoProjeto.programa}
                onChange={(e) =>
                    setNovoProjeto({
                        ...novoProjeto,
                        programa: e.target.value
                    })
                }
            />

            <input
                type="date"
                value={novoProjeto.dataInicio}
                onChange={(e) =>
                    setNovoProjeto({
                        ...novoProjeto,
                        dataInicio: e.target.value
                    })
                }
            />

            <input
                type="date"
                value={novoProjeto.dataFim}
                onChange={(e) =>
                    setNovoProjeto({
                        ...novoProjeto,
                        dataFim: e.target.value
                    })
                }
            />

            <button onClick={adicionarProjeto}>
                Criar Projeto
            </button>
            </div>
            <hr />

      <select
        onChange={(e) =>
          setProjetoSelecionado(
            e.target.value ? Number(e.target.value) : null
          )
        }
        value={projetoSelecionado || ""}
      >
        <option value="">Todos os projetos</option>

        {projetos.map((p) => (
          <option key={p.id} value={p.id}>
            {p.nome}
          </option>
        ))}
      </select>

      <div className="cards">
        <div className="card">
          <h3>Projetos</h3>
          <p>{projetos.length}</p>
        </div>

        <div className="card">
          <h3>Receita total</h3>
          <p>{dados.totalReceitas}</p>
        </div>

        <div className="card">
          <h3>Despesas realizadas</h3>
          <p>{dados.totalDespesas}</p>
        </div>

        <div className="card">
          <h3>Disponível</h3>
          <p>{dados.valorDisponivel}</p>
        </div>
      </div>
    </div>

    <hr />

        <h2>Receitas</h2>

        <table>
            <thead>
                <tr>
                    <th>Origem</th>
                    <th>Tipo</th>
                    <th>Valor</th>
                    <th>Ações</th>
                </tr>
            </thead>

            <tbody>
                {receitas.map((r) => (
                    <tr key={r.id}>
                        <td>{r.origem}</td>
                        <td>{r.tipo}</td>
                        <td>{r.valor}</td>
                        <td>
                            <button onClick={() => excluirReceita(r.id)}>
                                Excluir
                            </button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>

        <h2>Despesas</h2>

        <table>
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Categoria</th>
                    <th>Orçado</th>
                    <th>Realizado</th>
                    <th>Economia</th>
                    <th>Ações</th>
                </tr>
            </thead>

            <tbody>
                {despesas.map((d) => (
                    <tr key={d.id}>
                        <td>{d.nomeDespesa}</td>
                        <td>{d.categoria}</td>
                        <td>{d.valorOrcado}</td>
                        <td>{d.valorRealizado}</td>
                        <td>
                            {Number(d.valorOrcado) - Number(d.valorRealizado)}
                        </td>
                        <td>
                            <button onClick={() => excluirDespesa(d.id)}>
                                Excluir
                            </button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>

        <div className="forms-container">

            <div className="form-card">
                <h3>Nova Receita</h3>

                <input
                    placeholder="Origem"
                    value={novaReceita.origem}
                    onChange={(e) =>
                        setNovaReceita({
                            ...novaReceita,
                            origem: e.target.value
                        })
                    }
                />

                <input
                    placeholder="Tipo"
                    value={novaReceita.tipo}
                    onChange={(e) =>
                        setNovaReceita({
                            ...novaReceita,
                            tipo: e.target.value
                        })
                    }
                />

                <input
                    type="number"
                    placeholder="Valor"
                    value={novaReceita.valor}
                    onChange={(e) =>
                        setNovaReceita({
                            ...novaReceita,
                            valor: e.target.value
                        })
                    }
                />

                <button onClick={adicionarReceita}>
                    Adicionar Receita
                </button>
            </div>

            <div className="form-card">
                <h3>Nova Despesa</h3>

                <input
                    placeholder="Nome"
                    value={novaDespesa.nomeDespesa}
                    onChange={(e) =>
                        setNovaDespesa({
                            ...novaDespesa,
                            nomeDespesa: e.target.value
                        })
                    }
                />

                <input
                    placeholder="Categoria"
                    value={novaDespesa.categoria}
                    onChange={(e) =>
                        setNovaDespesa({
                            ...novaDespesa,
                            categoria: e.target.value
                        })
                    }
                />

                <input
                    type="number"
                    placeholder="Valor Orçado"
                    value={novaDespesa.valorOrcado}
                    onChange={(e) =>
                        setNovaDespesa({
                            ...novaDespesa,
                            valorOrcado: e.target.value
                        })
                    }
                />

                <input
                    type="number"
                    placeholder="Valor Realizado"
                    value={novaDespesa.valorRealizado}
                    onChange={(e) =>
                        setNovaDespesa({
                            ...novaDespesa,
                            valorRealizado: e.target.value
                        })
                    }
                />

                <button onClick={adicionarDespesa}>
                    Adicionar Despesa
                </button>
            </div>

        </div>
        </>
);}