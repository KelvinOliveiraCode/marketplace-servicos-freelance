# BRIEF — marketplace-servicos-freelance

## Projeto / Project
- Nome / Name: marketplace-servicos-freelance
- Área / Area: Full-Stack
- Stack: Java 21, Spring Boot 3 + React 18/TypeScript/Tailwind
- Saída / Output: `C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\marketplace-servicos-freelance\`

## Escopo do MVP / MVP scope
Anúncios, propostas e contratação de serviços freelance.
- Publicar anúncio (status disponível)
- Enviar proposta a um anúncio disponível
- Aceitar/rejeitar proposta pendente
- Fechar contratação a partir de proposta aceita (anúncio passa a contratado)

## Entregas / Deliverables
Backend (`src/main/java/com/kelvinoliveira/marketplace/...`):
- `pom.xml` — Spring Boot 3.5.16, Java 21, web + validation + test
- `MarketplaceApplication.java` — ponto de entrada
- `domain/modelo/` — `Anuncio`, `Proposta`, `Contratacao` (records) + enums de status
- `domain/dto/` — requests/responses com validação Jakarta + `ErroDetalhe`
- `domain/repository/` — repositórios em memória determinísticos (`AtomicLong`, `ConcurrentHashMap`)
- `domain/service/` — `AnuncioService`, `PropostaService`, `ContratacaoService` (regras de negócio)
- `domain/controller/` — REST controllers `/anuncios`, `/propostas`, `/contratacoes`
- `domain/exception/` — `GlobalExceptionHandler`, `ResourceNotFoundException`, `BusinessRuleException`
- `src/main/resources/application.properties`

Frontend (`frontend/`):
- Vite + React 18 + TypeScript strict + Tailwind CSS
- `src/api.ts` — cliente da API (`/anuncios`, `/propostas`, `/contratacoes`)
- `src/App.tsx` — listagem, criação de anúncio
- `src/components/AnuncioCard.tsx` — proposta, aceitar e contratar por anúncio

Testes:
- `src/test/java/com/kelvinoliveira/marketplace/domain/service/AnuncioServiceTest.java`
- `.../PropostaServiceTest.java`
- `.../ContratacaoServiceTest.java`
- `.../MarketplaceContextTest.java` (carga do contexto Spring)

Documentação:
- `README.md` — bilíngue PT-BR + EN (título, descrição, como usar, testes, MIT, autor Kelvin Oliveira)
- `BRIEF.md` — este arquivo
- `LICENSE` — MIT

## Como verificar / How to verify
1. Backend (Maven disponível offline):
   ```
   cd marketplace-servicos-freelance
   mvn test
   ```
   Esperado: 19 testes, 0 falhas, 0 erros, `BUILD SUCCESS`.
   Para iniciar a API: `mvn spring-boot:run` (porta 8080).
2. Frontend:
   ```
   cd frontend
   npm install
   npm run build   # tsc --noEmit && vite build (exit 0)
   npm run dev     # roda em http://localhost:5173
   ```
3. Fluxo de aceitação: `POST /anuncios` → `POST /propostas` → `POST /propostas/{id}/aceitar` → `POST /contratacoes`.

## Caminhos / Paths
- README: `C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\marketplace-servicos-freelance\README.md`
- BRIEF: `C:\Users\Kelvin\AppData\Local\Temp\opencode\github-staging\projects\marketplace-servicos-freelance\BRIEF.md`

## Resultado dos testes / Test result
Comando: `mvn test -B -o`
Resultado: 19 testes executados, 0 falhas, 0 erros, `BUILD SUCCESS`.
