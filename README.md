## Introdução e Arquitetura

O projeto consiste em um microserviço para realização das seguintes operações:

- Solicitar pagamento - POST

- Consultar pagamento - GET

- Atualizar/Cancelar pagamento - PUT

Foi utilizado Spring Boot para criação do microserviço e uma base de dados MySQL através de migração com uso do Flyway e H2 para criação do schema e tabelas. Desta forma, fica mais simples o deploy, manutenção e testes locais integrados.

Foi utilizado para execução dos testes da API o Junit, juntamente com o H2 e Flyway para migração e execução de scripts de pré-teste.

Para subir/deployar a aplicação, basta executar o arquivo Application.java.

## Exemplo de uso da API

- Consultar todos pagamentos - GET

    ````
    curl -H "Content-Type: application/json" -X GET http://localhost:8080/payments
    ````

- Solicitar pagamento - POST

    ````
    curl -H "Content-Type: application/json" -X POST http://localhost:8080/payment -d "{\"userId\":\"b3e54e3d-eb6c-4cf1-983c-f497549863a8\",\"paymentDescription\":\"pagamento teste\",\"referenceNumber\":\"12345678910\",\"paymentDate\":\"2019-03-31\",\"amount\":20}"
    ````

    Output:
    ````
    {
        "paymentCode":"c501ae84",
        "userId":"b3e54e3d-eb6c-4cf1-983c-f497549863a8",
        "paymentDescription":"pagamento teste",
        "referenceNumber":"12345678910",
        "paymentDate":"2019-03-31",
        "amount":20,
        "status":"PENDING"
    }
    ````

- Consultar pagamento - GET

    ````
    curl -H "Content-Type: application/json" -X GET http://localhost:8080/payment/c501ae84
    ````

    Output:
    ````
    {
        "paymentCode":"c501ae84",
        "userId":"b3e54e3d-eb6c-4cf1-983c-f497549863a8",
        "paymentDescription":"pagamento teste",
        "referenceNumber":"12345678910",
        "paymentDate":"2019-03-31",
        "amount":20,
        "status":"PENDING"
    }
    ````

- Atualizar/Cancelar pagamento - PUT

    ````
    curl -H "Content-Type: application/json" -X PUT http://localhost:8080/payment/c501ae84 -d "{\"paymentCode\":\"c501ae84\",\"userId\":\"b3e54e3d-eb6c-4cf1-983c-f497549863a8\",\"paymentDescription\":\"Change payment date\",\"referenceNumber\":\"12345678910\",\"paymentDate\":\"2019-03-31\",\"amount\":20,\"status\":\"CANCELED\"}"
    ````
    
    Output:
    ````
    {
        "paymentCode":"c501ae84",
        "userId":"b3e54e3d-eb6c-4cf1-983c-f497549863a8",
        "paymentDescription":"Change payment date",
        "referenceNumber":"12345678910",
        "paymentDate":"2019-03-31",
        "amount":20,
        "status":"CANCELED"
    }
    ````