# AGENTS.md

## Frontend (obrigatorio)

Este projeto deve usar somente Bootstrap no frontend.

Regras:
- Usar exclusivamente classes/utilitarios/componentes do Bootstrap.
- Nao criar nem manter CSS customizado em `src/main/resources/static/css` (ou equivalente).
- Nao adicionar frameworks CSS alternativos (Tailwind, Bulma, Materialize, etc.).
- Nao usar CDN para Bootstrap quando o asset local estiver disponivel no projeto (WebJar/local).
- Qualquer ajuste visual deve ser feito com recursos nativos do Bootstrap.

## Validacao antes de concluir alteracoes de frontend

- Confirmar que nao existe import/referencia de arquivo CSS proprio do projeto.
- Confirmar que a pagina carrega apenas Bootstrap como fonte de estilo.
