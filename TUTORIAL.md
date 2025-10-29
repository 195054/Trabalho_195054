Criando o Diálogo no index.xhtml

primeiro passo é criarmos o dialog dentro <h:form id="formPrincipal"> existente.

Dialog criado por mim de exemplo:
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
                         org.primefaces.PrimeFaces.current().ajax().update("formPrincipal");
                         oncomplete="PF('dlgAutor').hide()"/>
        <p:commandButton value="Cancelar" icon="pi pi-times"
                         onclick="PF('dlgAutor').hide()" type="button"/>
    </f:facet>
</p:dialog>



widgetVar="dlgAutor": nome JavaScript do modal (usado para abrir/fechar).
modal="true": bloqueia a tela de fundo enquanto o diálogo está aberto.
p:messages: exibe mensagens de sucesso ou erro.
process="@form": processa apenas os campos do modal.
org.primefaces.PrimeFaces.current().ajax().update("formPrincipal"): recarrega toda a tabela principal de autores.
oncomplete: executa o JS para fechar o modal depois do Ajax.


Criando o Botão que Abre o Modal

Dentro da aba Autores, adicione um botão para abrir o diálogo.

Código:
<p:commandButton value="Novo Autor" icon="pi pi-user-plus"
                 actionListener="#{bibliotecaBean.novoAutorDialog}"
                 oncomplete="PF('dlgAutor').show()"
                 styleClass="p-button-success"/>




Chama o método novoAutorDialog() no BibliotecaBean para limpar o formulário.
Após o Ajax, o comando PF('dlgAutor').show() abre o modal.
O actionListener não precisa recarregar nada serve apenas para inicializar o bean.



os métodos que o modal usa no Bean:

Método para abrir o modal:
public void novoAutorDialog() {
    novoAutor = new Autor(); // limpa os dados anteriores
}

Método para salvar o autor:
public String salvarAutor() {
    try {
        if (novoAutor.getNome() == null || novoAutor.getNome().isBlank()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Informe o nome do autor!", null));
            return null;
        }

        service.salvarAutor(novoAutor);
        carregarDados();
        carregarEstatisticas();
        novoAutor = new Autor();

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Autor salvo com sucesso!", null));

        return null; // permanece na mesma página
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar autor!", null));
        return null;
    }
}

service.salvarAutor() grava o novo autor no banco via JPA.
carregarDados() e carregarEstatisticas() recarregam a lista e os contadores.
Retornar null mantém o usuário na mesma tela JSF.



As mensagens aparecem automaticamente no <p:messages>.

Persistência no AutorRepository.java



Garanta que o repositório sincroniza com o banco logo após salvar.

public void persistir(Autor autor) {
    if (autor.getId() == null) {
        em.persist(autor);
    } else {
        em.merge(autor);
    }
    em.flush(); // garante escrita imediata
    em.clear(); // limpa cache do EntityManager
}
