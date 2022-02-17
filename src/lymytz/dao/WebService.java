/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.dao;

//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.UriBuilder;
import lymytz.service.application.ManagedApplication;

/**
 *
 * @author LENOVO
 */
public class WebService {

    public WebService() {
    }

    ManagedApplication page;

    public WebService(ManagedApplication page) {
        this.page = page;
    }

//    public boolean serverOnline(String service) {
//        try {
//            Client clt = Client.create(new DefaultClientConfig());
//            URI uri = getUriAdresse(service);
//            if (uri != null) {
//                ClientResponse response = clt.resource(uri).path("serverOnline")
//                        .type(MediaType.APPLICATION_JSON)
//                        .get(ClientResponse.class);
//                return (response.getStatus() >= 200 && response.getStatus() < 300);
//            } else {
//                return false;
//            }
//        } catch (Exception ex) {
//        }
//        return false;
//    }
//    private URI getUriAdresse(String service) {
//        String adresse;
//        String port;
//        ParamConnection param = new ParamConnection();
//        param.readFile(LymytzSell.getFileInputStream());
//        if ((param.getHostWeb() != null ? !param.getHostWeb().trim().isEmpty() : false) && (param.getPortWeb() != null ? !param.getPortWeb().trim().isEmpty() : false)) {
//            adresse = param.getHostWeb();
//            port = param.getPortWeb();
//            return UriBuilder.fromUri("http://" + adresse + ":" + port + "/Lymytz_Web/ws/services/" + service).build();
//        }
//        return null;
//    }
//
//    private URI getUriAdresseCompta() {
//        String adresse;
//        String port;
//        ParamConnection param = new ParamConnection();
//        param.readFile(LymytzSell.getFileInputStream());
//        if ((param.getHostWeb() != null ? !param.getHostWeb().trim().isEmpty() : false) && (param.getPortWeb() != null ? !param.getPortWeb().trim().isEmpty() : false)) {
//            adresse = param.getHostWeb();
//            port = param.getPortWeb();
//            return UriBuilder.fromUri("http://" + adresse + ":" + port + "/Lymytz_Web/ws/services/compta").build();
//        }
//        return null;
//    }
//    public List<YvsComContenuDocStock> findTransfert(long idDepot) {
//        List<YvsComContenuDocStock> re = new ArrayList<>();
//        if (serverOnline("commercial")) {
//            DefaultClientConfig config = new DefaultClientConfig();
//            config.getClasses().add(JacksonJsonProvider.class);
//            Client clt = Client.create(config);
//            WebResource webRes = clt.resource(getUriAdresse("commercial")).path("getDocTransfert");
//            WebResource.Builder builder = webRes.accept(MediaType.APPLICATION_JSON).header("content-type", MediaType.TEXT_PLAIN);
//            builder.header("depot", new String("" + idDepot));
//            ClientResponse response = builder.post(ClientResponse.class);
//            if (response.getStatus() != 200) {
//                LymytzService.openAlertDialog("Une erreur inconnue s'est produite sur le serveur distant! Veuillez contacter votre administrateur", " Fatal Error", "Erreur sur le serveur distant", Alert.AlertType.ERROR);
//            } else {
//                GenericType<List<YvsComContenuDocStock>> generic = new GenericType<List<YvsComContenuDocStock>>() {
//                    //
//
//                };
//                re = response.getEntity(generic);
//            }
//        }
//        return re;
//    }
    public Long countWaitingTransfert(long idDepot) {
//        try {
//            if (serverOnline("commercial")) {
//                page.IMG_WIFI.setImage(new Image(LocalLoader.class.getResource("icones/i_wifi_connect.png").toExternalForm()));
//                DefaultClientConfig config = new DefaultClientConfig();
//                config.getClasses().add(JacksonJsonProvider.class);
//                Client clt = Client.create(config);
//                WebResource webRes = clt.resource(getUriAdresse("commercial")).path("countTransfert");
//                WebResource.Builder builder = webRes.accept(MediaType.APPLICATION_JSON).header("content-type", MediaType.TEXT_PLAIN);
//                builder.header("depot", new String("" + idDepot));
//                ClientResponse response = builder.post(ClientResponse.class);
//                if (response.getStatus() != 200) {
//                    System.err.println(" Not found");
//                    LymytzService.openAlertDialog("Une erreur inconnue s'est produite sur le serveur distant! Veuillez contacter votre administrateur", " Fatal Error", "Erreur sur le serveur distant", Alert.AlertType.ERROR);
//                } else {
//                    GenericType<List<YvsComContenuDocStock>> generic = new GenericType<List<YvsComContenuDocStock>>() {
//                        //
//
//                    };
//                    Long re = response.getEntity(Long.class);
//                    return re;
//                }
//            } else {
//                page.IMG_WIFI.setImage(new Image(LocalLoader.class.getResource("icones/i_wifi_disconnect.png").toExternalForm()));
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return 0l;
    }

//    public ResultatAction vaideTransferts(long idDoc, long idUserAgence) {
//        if (serverOnline("commercial")) {
//            DefaultClientConfig config = new DefaultClientConfig();
//            config.getClasses().add(JacksonJsonProvider.class);
//            Client clt = Client.create(config);
//            WebResource webRes = clt.resource(getUriAdresse("commercial")).path("valideDocTransfert");
//            WebResource.Builder builder = webRes.accept(MediaType.APPLICATION_JSON).header("content-type", MediaType.TEXT_PLAIN);
//            builder.header("idDoc", new String("" + idDoc));
//            builder.header("idUser", new String("" + idUserAgence));
//            ClientResponse response = builder.post(ClientResponse.class);
//            if (response.getStatus() != 200) {
//
//            } else {
//                GenericType<ResultatAction> generic = new GenericType<ResultatAction>() {
//                    //
//
//                };
//                ResultatAction result = response.getEntity(generic);
//                return result;
//            }
//        }
//        return null;
//    }
//    public ResultatAction vaideOneLineTransferts(long idLine, long idUserAgence) {
//        if (serverOnline("commercial")) {
//            DefaultClientConfig config = new DefaultClientConfig();
//            config.getClasses().add(JacksonJsonProvider.class);
//            Client clt = Client.create(config);
//            WebResource webRes = clt.resource(getUriAdresse("commercial")).path("valideLineTransfert");
//            WebResource.Builder builder = webRes.accept(MediaType.APPLICATION_JSON).header("content-type", MediaType.TEXT_PLAIN);
//            builder.header("idLine", new String("" + idLine));
//            builder.header("idUser", new String("" + idUserAgence));
//            ClientResponse response = builder.post(ClientResponse.class);
//            if (response.getStatus() != 200) {
//
//            } else {
//                GenericType<ResultatAction> generic = new GenericType<ResultatAction>() {
//                    //
//
//                };
//                ResultatAction result = response.getEntity(generic);
//                return result;
//            }
//        }
//        return null;
//    }
//    public ResultatAction saveVirement(long cSource, long cCible, Double montant, long userAgence, Double vrstAttendu, long header) {
//        if (serverOnline("compta")) {
//            DefaultClientConfig config = new DefaultClientConfig();
//            config.getClasses().add(JacksonJsonProvider.class);
//            Client clt = Client.create(config);
//            WebResource webRes = clt.resource(getUriAdresseCompta()).path("saveVirementCaisse");
//            WebResource.Builder builder = webRes.accept(MediaType.APPLICATION_JSON).header("content-type", MediaType.TEXT_PLAIN);
//            builder.header("caisseSource", new String("" + cSource));
//            builder.header("caisseCible", new String("" + cCible));
//            builder.header("montant", new String("" + montant));
//            builder.header("idUser", new String("" + userAgence));
//            builder.header("header", new String("" + header));
//            builder.header("versementAttendu", new String("" + vrstAttendu));
//            ClientResponse response = builder.post(ClientResponse.class);
//            if (response.getStatus() != 200) {
//            } else {
//                GenericType<ResultatAction> generic = new GenericType<ResultatAction>() {
//                    //
//                };
//                ResultatAction result = response.getEntity(generic);
//                return result;
//            }
//        }
//        return null;
//    }
    
}
