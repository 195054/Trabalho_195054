
# Como Criar e Configurar um Dialog no Projeto "Biblioteca Digital"

## Introdução (O que e Por Quê)

Neste tutorial, será mostrado como implementar e configurar um **modal (dialog)** usando o componente `<p:dialog>` do **PrimeFaces**, integrado ao projeto **Biblioteca Digital**.

O objetivo é permitir que o usuário **cadastre novos autores e livros sem sair da tela principal**, tornando o sistema mais dinâmico, interativo e agradável de usar.





## Pré-requisitos

- Projeto **Biblioteca Digital** funcional no Quarkus.  
- Dependência do **PrimeFaces** no `pom.xml`.  
- Beans e entidades já configurados (`BibliotecaBean`, `Autor`, `Livro`, `Emprestimo`).  






## Implementação Completa

### Adicionando a dependência do PrimeFaces

No arquivo `pom.xml`:

```xml
<dependency>
    <groupId>org.primefaces</groupId>
    <artifactId>primefaces</artifactId>
    <version>13.0.0</version>
</dependency>
```

Essa biblioteca fornece os componentes de interface gráfica usados no JSF






### Criando o Botão que Abre o Modal

No arquivo `index.xhtml`, dentro da aba **Autores**, adicione:

```xml
<p:commandButton value="Novo Autor" icon="pi pi-user-plus"
                 actionListener="#{bibliotecaBean.novoAutorDialog}"
                 oncomplete="PF('dlgAutor').show()" styleClass="p-button-success"/>
```

**Explicação:**
- `actionListener`: executa o método `novoAutorDialog()` no `BibliotecaBean`.  
- `oncomplete`: abre o modal usando o widget `PF('dlgAutor').show()`.  







### Criando o Modal (Dialog)

Ainda no `index.xhtml`, adicione o seguinte bloco:

```xml
<p:dialog header="Novo Autor" widgetVar="dlgAutor" modal="true" width="400">
    <p:messages id="msgAutor" autoUpdate="true" closable="true"/>

    <h:panelGrid columns="2" cellpadding="5">
        <h:outputLabel value="Nome:"/>
        <p:inputText value="#{bibliotecaBean.novoAutor.nome}" 
                     required="true" 
                     requiredMessage="O nome do autor é obrigatório."
                     validateClient="true"/>

        <h:outputLabel value="Email:"/>
        <p:inputText value="#{bibliotecaBean.novoAutor.email}"/>

        <h:outputLabel value="Biografia:"/>
        <p:inputTextarea value="#{bibliotecaBean.novoAutor.biografia}" rows="3" cols="20"/>
    </h:panelGrid>

    <f:facet name="footer">
        <p:commandButton value="Salvar" icon="pi pi-check"
                         action="#{bibliotecaBean.salvarAutor}"
                         process="@form"
                         update=":formPrincipal"
                         oncomplete="PF('dlgAutor').hide()"/>

        <p:commandButton value="Cancelar" icon="pi pi-times"
                         onclick="PF('dlgAutor').hide()" type="button"/>
    </f:facet>
</p:dialog>
```

`widgetVar="dlgAutor"`: nome usado no JavaScript para abrir/fechar o modal.  
`modal="true"`: bloqueia interação com o restante da tela.  
`oncomplete="PF('dlgAutor').hide()"`: fecha automaticamente após salvar.  






### Implementando no Bean — `BibliotecaBean.java`

```java
@Named("bibliotecaBean")
@ViewScoped
public class BibliotecaBean implements Serializable {

    @Inject
    private BibliotecaService service;

    private Autor novoAutor = new Autor();
    private List<Autor> autores;

    @PostConstruct
    public void init() {
        autores = service.listarTodosAutores();
    }

    public void novoAutorDialog() {
        novoAutor = new Autor(); 
    }

    public void salvarAutor() {
        try {
            service.salvarAutor(novoAutor);
            autores = service.listarTodosAutores();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Autor salvo com sucesso!", null));

            org.primefaces.PrimeFaces.current().ajax().update("formPrincipal");

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar autor.", null));
        }
    }

    public List<Autor> getAutores() { return autores; }
    public Autor getNovoAutor() { return novoAutor; }
}
```


### Resultado em tela

<p align="center">
  <img src="image.png" width="400">
  <img src="image-1.png" width="400">
</p>



### Testando a Funcionalidade

Execute o projeto:
## ./mvnw quarkus:dev


Acesse no navegador:

**http://localhost:8080**

1. Clique na aba **Autores**.  
2. Pressione **“Novo Autor”**.  
3. O modal aparecerá centralizado.  
4. Após preencher e salvar, o autor será adicionado automaticamente à tabela.



## Benefícios da Implementação

- Melhora a **usabilidade** e **responsividade** da interface.  
- Evita redirecionamentos desnecessários.  
- Permite um fluxo de trabalho mais fluido e moderno.



Referências
1. [PrimeFaces — Dialog Component](https://primefaces.github.io/primefaces/13_0_0/#/components/dialog)
2. [Jakarta Faces Specification](https://jakarta.ee/specifications/faces/)
3. [PrimeFaces Showcase — Dialogs](https://www.primefaces.org/showcase)




