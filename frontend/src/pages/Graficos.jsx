import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./../styles/graficos.css";

export default function Graficos() {

    const navigate = useNavigate();

    const [dados, setDados] = useState(null);

    const [projetos, setProjetos] = useState([]);

    const [projetoSelecionado, setProjetoSelecionado] = useState("");

    useEffect(() => {

        async function carregar() {

            const token = localStorage.getItem("token");

            // Dashboard geral ou de um projeto
            const url = projetoSelecionado
                ? `http://localhost:5279/api/Projetos/${projetoSelecionado}/dashboard`
                : "http://localhost:5279/api/Dashboard/me";

            const response = await axios.get(
                url,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            setDados(response.data);

            // Lista de projetos
            const projetosResponse = await axios.get(
                "http://localhost:5279/api/Pesquisadores/me/projetos",
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            setProjetos(projetosResponse.data);
        }

        carregar();

    }, [projetoSelecionado]);

    if (!dados) return <h2>Carregando...</h2>;

    return (
        <div className="graficos">

            <h1>Indicadores Financeiros</h1>

            <button
                onClick={() => navigate("/dashboard")}
            >
                Voltar
            </button>

            <select
                value={projetoSelecionado}
                onChange={(e) => setProjetoSelecionado(e.target.value)}
            >

                <option value="">
                    Todos os projetos
                </option>

                {projetos.map((p) => (
                    <option
                        key={p.id}
                        value={p.id}
                    >
                        {p.nome}
                    </option>
                ))}

            </select>

            <div className="indicador">

                <h3>Receita Total</h3>

                <div className="barra">

                    <div
                        className="receita"
                        style={{
                            width: "100%"
                        }}
                    ></div>

                </div>

                <p>R$ {dados.totalReceitas}</p>

            </div>

            <div className="indicador">

                <h3>Despesas</h3>

                <div className="barra">

                    <div
                        className="despesa"
                        style={{
                            width:
                                (dados.totalDespesas /
                                    dados.totalReceitas) *
                                100 + "%"
                        }}
                    ></div>

                </div>

                <p>R$ {dados.totalDespesas}</p>

            </div>

            <div className="indicador">

                <h3>Disponível</h3>

                <div className="barra">

                    <div
                        className="disponivel"
                        style={{
                            width:
                                (dados.valorDisponivel /
                                    dados.totalReceitas) *
                                100 + "%"
                        }}
                    ></div>

                </div>

                <p>R$ {dados.valorDisponivel}</p>

            </div>

        </div>
    );
}