# AGENTS.md

## API (obrigatorio)

Regras:
- Definir rotas diretamente nas anotacoes de metodo (`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, etc.).
- Nao usar `@RequestMapping` em nivel de classe para compor prefixo de rota.

## Modelagem JSON (obrigatorio)

Regras:
- Quando um campo for persistido como JSON em entidade JPA, modelar como inner class dentro da propria entidade.
- Nao criar classe externa para payload JSON de entidade quando houver padrao interno ja existente (ex.: `PrInfo`).
- Usar mapeamento JSON nativo com Hibernate 6 (`@JdbcTypeCode(SqlTypes.JSON)`) no atributo da entidade.
