# Marketplace de Serviços Freelance / Freelance Services Marketplace

## Sobre / About

**PT-BR.** API REST para marketplace de serviços freelance com escopo mínimo viável (MVP): publicar anúncios de serviços, enviar propostas e fechar contratações. Backend em Java 21 com Spring Boot 3 e frontend mínimo em React + TypeScript + Tailwind. Os dados ficam em memória (sem banco externo), o que torna o projeto fácil de rodar e testar localmente.

**EN.** REST API for a freelance services marketplace with a minimum viable product (MVP) scope: publish service listings, send proposals, and close hires. Backend in Java 21 with Spring Boot 3 and a minimal frontend in React + TypeScript + Tailwind. Data is kept in memory (no external database), so the project is easy to run and test locally.

## Escopo do MVP / MVP scope

- **Anúncios:** publicar, listar disponíveis e buscar por ID.
- **Propostas:** enviar para um anúncio disponível, aceitar ou rejeitar uma proposta pendente.
- **Contratação:** fechar a contratação a partir de uma proposta aceita; o anúncio passa a status contratado e não aceita mais propostas.

## Stack

- Java 21, Spring Boot 3 (web, validation)
- JUnit 5 + AssertJ (testes)
- React 18, TypeScript (strict), Tailwind CSS, Vite
- Build: Maven (backend), npm (frontend)

## Estrutura do repositório / Repository structure

```
marketplace-servicos-freelance/
├── pom.xml
├── README.md
├── BRIEF.md
├── LICENSE
├── src/main/java/com/kelvinoliveira/marketplace/   # backend Java
└── frontend/                                       # frontend React + TS + Tailwind
    ├── package.json
    ├── vite.config.ts
    ├── tailwind.config.js
    ├── tsconfig.json
    └── src/
        ├── main.tsx
        ├── App.tsx
        ├── index.css
        ├── api.ts
        └── components/AnuncioCard.tsx
```

## Como usar / How to use

### Pré-requisitos / Prerequisites

- JDK 21
- Apache Maven 3.9+
- Node.js 20+ e npm

### Backend / Backend

Abra a pasta do projeto e rode:

```
mvn test
```

Os testes usam o repositório Maven local; não é necessário banco de dados externo.

Para iniciar a API:

```
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

### Frontend / Frontend

```
cd frontend
npm install
npm run dev
```

O frontend roda em `http://localhost:5173` e consome a API em `http://localhost:8080`. Inicie o backend antes de usar o frontend.

## Rodando os testes / Running tests

```
cd marketplace-servicos-freelance   # pasta raiz
mvn test
```

Resultado esperado: 19 testes, 0 falhas, 0 erros (BUILD SUCCESS).

Os testes cobrem: criação de anúncios com status disponível, IDs sequenciais, listagem apenas de anúncios disponíveis, envio/aceitação/rejeição de propostas, regras de negócio (proposta não aceita, anúncio não disponível, contrato duplicado) e carga do contexto Spring.

## Endpoints da API / API endpoints

| Método | Caminho | Descrição / Description |
|--------|---------|-------------------------|
| POST | `/anuncios` | Publica um anúncio / Publish a listing |
| GET | `/anuncios` | Lista anúncios disponíveis / List available listings |
| GET | `/anuncios/{id}` | Busca anúncio por ID / Get listing by ID |
| POST | `/propostas` | Envia uma proposta / Send a proposal |
| POST | `/propostas/{id}/aceitar` | Aceita uma proposta pendente / Accept a pending proposal |
| POST | `/propostas/{id}/rejeitar` | Rejeita uma proposta pendente / Reject a pending proposal |
| GET | `/propostas/anuncios/{anuncioId}` | Lista propostas de um anúncio / List proposals of a listing |
| POST | `/contratacoes` | Fecha uma contratação / Close a hire |
| GET | `/contratacoes/{id}` | Busca contratação por ID / Get hire by ID |

Fluxo típico / Typical flow: `POST /anuncios` → `POST /propostas` → `POST /propostas/{id}/aceitar` → `POST /contratacoes`.

## Licença / License

MIT. Veja o arquivo `LICENSE`.

MIT. See the `LICENSE` file.

## Autor / Author

Kelvin Oliveira
