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

## Validacao de API (obrigatorio)

Regras:
- Preferir Bean Validation (`jakarta.validation`) com anotacoes nos DTOs (`@NotBlank`, `@NotNull`, etc.) e `@Valid` nos endpoints.
- Nao fazer validacao manual com `if` para campos obrigatorios em controllers/APIs quando Bean Validation atender o caso.

## Imports (obrigatorio)

Regras:
- Se houver 3 ou mais imports do mesmo pacote, usar import com wildcard (`*`) para esse pacote.
