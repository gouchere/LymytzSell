/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lymytz.service.application.synchro.TableList;

/**
 *
 * @author Admin
 */
public class Constantes {
    /*Constante DateFormat*/

    public static Long localId = -999L;
    public static List<String> LISTEN_TABLE;   //tables dont on doit surveiller l'activité sur le serveur distant
    public static List<TableList> ALLENTITY_BASE = new ArrayList<>();   //données de base qui doivent provenir du serveur distant
    public static List<TableList> ALLENTITY_DATA = new ArrayList<>();   //données de base qui doivent partir du serveur local
    public static String SEPARATOR = System.getProperty("file.separator");
    public static final DateFormat dfh = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public static final DateFormat dfD = new SimpleDateFormat("dd-MM-yyyy");
    public static final DateFormat HMS = new SimpleDateFormat("HH:mm:ss");
    public static final DateFormat dfH = new SimpleDateFormat("HH:mm");
    public static final DecimalFormat nbf = new DecimalFormat("#,###");

    public static final double WIDTH_ICON_TOOL_BAR = 25;
    public static final double HEIGHT_ICON_TOOL_BAR = 25;
    public static final double WIDTH_ICON_MENU_BAR = 15;
    public static final double HEIGHT_ICON_MENU_BAR = 15;

    public static final Integer NB_FAILED = 3;

    public static String PARAM_TABLE;
    //Type de documents vente
    public static final String TYPE_FV = "FV"; //Facture Vente
    public static final String TYPE_FV_NAME = "Facture Vente"; //
    public static final String TYPE_FRV = "FRV"; //Facture Retour Vente
    public static final String TYPE_FRV_NAME = "Facture Retour Vente"; //
    public static final String TYPE_FAV = "FAV"; //Facture Avoir Vente
    public static final String TYPE_FAV_NAME = "Facture Avoir Vente"; //
    public static final String TYPE_BCV = "BCV"; //Bon Commande Vente
    public static final String TYPE_BCV_NAME = "Bon Commande Vente"; //
    public static final String TYPE_BLV = "BLV"; //Bon Livraison Vente
    public static final String TYPE_BLV_NAME = "Bon Livraison Vente"; //
    public static final String TYPE_BRV = "BRV"; //Bon Retour Vente
    public static final String TYPE_BRV_NAME = "Bon Retour Vente"; //
    public static final String TYPE_BAV = "BAV"; //Bon Avoir Vente
    public static final String TYPE_BAV_NAME = "Bon Avoir Vente"; //

    public static final String NATURE_DOC_VENTE_VENTE = "VENTE"; //
    public static final String NATURE_DOC_VENTE_LOCATION = "LOCATION"; //

    //Type de documents autres
    public static final String TYPE_PIECE_COMPTABLE_NAME = "PIECE COMPTABLE"; //
    public static final String TYPE_PT = "PT"; //Piece Tresorerie
    public static final String TYPE_PT_NAME = "Piece Tresorerie"; //
    public static final String TYPE_PC_NAME = "Piece Caisse"; //
    public static final String TYPE_PC_ACHAT_NAME = "Piece Caisse Achat"; //
    public static final String TYPE_PC_VENTE_NAME = "Piece Caisse Vente"; //
    public static final String TYPE_CONTRAT_CLIENT_NAME = "Contrat Client";

    public static final String TYPE_PT_AVANCE_VENTE = "Piece Avance Vente";

    public static final String MOUV_CAISS_ENTREE = "R";
    public static final String MOUV_CAISS_SORTIE = "D";

    public static final char STATUT_DOC_ATTENTE = 'W', STATUT_DOC_ENCOUR = 'R', STATUT_DOC_VALIDE = 'V', STATUT_DOC_SUSPENDU = 'S', STATUT_DOC_ANNULE = 'A', STATUT_DOC_SOUMIS = 'U',
            STATUT_DOC_TERMINE = 'T', STATUT_DOC_CLOTURE = 'C', STATUT_DOC_PAYER = 'P', STATUT_DOC_LIVRER = 'L', STATUT_DOC_EDITABLE = 'E', STATUT_DOC_RENVOYE = 'B';

    //Etat et Statut
    public static final String ETAT_ATTENTE = "W";
    public static final String ETAT_EDITABLE = "E";
    public static final String ETAT_SOUMIS = "U";
    public static final String ETAT_REGLE = "P";
    public static final String ETAT_VALIDE = "V";
    public static final String ETAT_ANNULE = "A";
    public static final String ETAT_RELANCE = "C";
    public static final String ETAT_LIVRE = "L";
    public static final String ETAT_PROD_LANCE = "L";
    public static final String ETAT_ENCOURS = "R";
    public static final String ETAT_RENVOYE = "B";
    public static final String ETAT_JUSTIFIE = "J";
    public static final String ETAT_INJUSTIFIE = "I";
    public static final String ETAT_SUSPENDU = "S";
    public static final String ETAT_TERMINE = "T";
    public static final String ETAT_CLOTURE = "F";
    public static final String ETAT_INCOMPLET = "M";

    public static final String CAT_MARCHANDISE = "MARCHANDISE", CAT_SERVICE = "SERVICE";
    public static final String CAT_PSF = "PSF";
    public static final String CAT_MP = "MP";
    public static final String CAT_PF = "PF";

    public static final String INSERT_ACTION = "INSERT";
    public static final String UPDATE_ACTION = "UPDATE";
    public static final String DELETE_ACTION = "DELETE";
    public static final String TRONCATE_ACTION = "TRONCATE";

    public static final String SOCIETE = "SOCIETE";
    public static final String AGENCE = "AGENCE";
    public static final String DEPOT = "DEPOT";
    public static final String JOURNAL = "JOURNAL";
    public static final String POINTVENTE = "POINTVENTE";
    public static final String CAISSE = "CAISSE";
    public static final String AUTRES = "AUTRES";

    public static final String MODE_PAIEMENT_ESPECE = "ESPECE";

    public static final String APPS_MODE_BOTH = "BOTH";
    public static final String APPS_MODE_SINGLE = "SINGLE";
    public static final String APPS_ENV_PROD = "PRODUCTION";
    public static final String APPS_ENV_DEV = "DEVELOPPEMENT";
    public static final String TYPE_PRINT_A4 = "A4";
    public static final String TYPE_PRINT_TICKET = "TICKET";

    //Données de base
    public static final String TABLE_ARTICLE_NAME = "Articles";
    public static final String TABLE_ARTICLE_CODE = "yvs_base_articles";
    public static final String TABLE_SOCIETE_NAME = "Societe";
    public static final String TABLE_SOCIETE_CODE = "yvs_societes";
    public static final String TABLE_AGENCE_NAME = "Agences";
    public static final String TABLE_AGENCE_CODE = "yvs_agences";
    public static final String TABLE_USERS_NAME = "Utilisateurs";
    public static final String TABLE_USERS_CODE = "yvs_users";
    public static final String TABLE_FAMILLE_NAME = "Familles Article";
    public static final String TABLE_FAMILLE_CODE = "yvs_base_famille_article";
    public static final String TABLE_GROUPE_ART_NAME = "Groupes Article";
    public static final String TABLE_GROUPE_ART_CODE = "yvs_base_groupes_article";
    public static final String TABLE_CODE_BARRE_NAME = "Codes barres";
    public static final String TABLE_CODE_BARRE_CODE = "yvs_base_article_code_barre";
    public static final String TABLE_DESC_ART_NAME = "Description Articles";
    public static final String TABLE_DESC_ART_CODE = "yvs_base_article_description";
    public static final String TABLE_CATEGORIE_COMPTABLE_NAME = "Categorie comptable";
    public static final String TABLE_CATEGORIE_COMPTABLE_CODE = "yvs_base_categorie_comptable";
    public static final String TABLE_ARTICLE_CATEGORIE_COMPTABLE_NAME = "Articles Categorie comptable";
    public static final String TABLE_ARTICLE_CATEGORIE_COMPTABLE_CODE = "yvs_base_article_categorie_comptable";
    public static final String TABLE_ARTICLE_CATEGORIE_COMPTABLE_TAXE_NAME = "Articles Taxes";
    public static final String TABLE_ARTICLE_CATEGORIE_COMPTABLE_TAXE_CODE = "yvs_base_article_categorie_comptable_taxe";
    public static final String TABLE_UNITE_MESURE_NAME = "Unites de mesure";
    public static final String TABLE_UNITE_MESURE_CODE = "yvs_base_unite_mesure";
    public static final String TABLE_ELEMENT_REFERENCE_NAME = "Element de reference";
    public static final String TABLE_ELEMENT_REFERENCE_CODE = "yvs_base_element_reference";
    public static final String TABLE_MODELE_DE_REGLEMENT_NAME = "Modele de reglement";
    public static final String TABLE_MODELE_DE_REGLEMENT_CODE = "yvs_base_model_reglement";
    public static final String TABLE_MODE_DE_REGLEMENT_NAME = "Mode de reglement";
    public static final String TABLE_MODE_DE_REGLEMENT_CODE = "yvs_base_mode_reglement";
    public static final String TABLE_MODELE_DE_REFERENCE_NAME = "Modèles De Référence";
    public static final String TABLE_MODELE_DE_REFERENCE_CODE = "yvs_base_modele_reference";
    public static final String TABLE_TAXES_NAME = "Taxes";
    public static final String TABLE_TAXES_CODE = "yvs_base_taxes";
    public static final String TABLE_TIERS_NAME = "Tiers";
    public static final String TABLE_TIERS_CODE = "yvs_base_tiers";
    public static final String TABLE_TELEPHONE_TIERS_NAME = "Téléphone Tiers";
    public static final String TABLE_TELEPHONE_TIERS_CODE = "yvs_base_tiers_telephone";
    public static final String TABLE_CLIENTS_NAME = "Clients";
    public static final String TABLE_CLIENTS_CODE = "yvs_com_client";
    public static final String TABLE_POINT_DE_VENTE_NAME = "Points De Ventes";
    public static final String TABLE_POINT_DE_VENTE_CODE = "yvs_base_point_vente";
    public static final String TABLE_CLASSE_STAT_NAME = "Classes Statistiques";
    public static final String TABLE_CLASSE_STAT_CODE = "yvs_base_classes_stat";
    public static final String TABLE_TRANCHE_HORAIRE_NAME = "Tranches Horaire";
    public static final String TABLE_TRANCHE_HORAIRE_CODE = "yvs_grh_tranche_horaire";

    public static final String TABLE_DICTIONNAIRES_NAME = "Dictionnaires";
    public static final String TABLE_DICTIONNAIRES_CODE = "yvs_dictionnaire";
    public static final String TABLE_CATEGORIE_CLIENT_NAME = "Categorie clients";
    public static final String TABLE_CATEGORIE_CLIENT_CODE = "yvs_base_categorie_client";
    public static final String TABLE_DEPOT_NAME = "Categorie Dépot";
    public static final String TABLE_DEPOT_CODE = "yvs_base_depots";
    public static final String TABLE_PLAN_TARIFAIRE_NAME = "Plan Tarifaire";
    public static final String TABLE_PLAN_TARIFAIRE_CODE = "yvs_base_plan_tarifaire";
    public static final String TABLE_PLAN_REMISE_NAME = "Plan Remise";
    public static final String TABLE_PLAN_REMISE_CODE = "yvs_com_remise";
    public static final String TABLE_RABAIS_NAME = "Plan Rabais";
    public static final String TABLE_RABAIS_CODE = "yvs_com_rabais";
    public static final String TABLE_GRILLE_PLAN_REMISE_NAME = "Grille Plan Remise";
    public static final String TABLE_GRILLE_PLAN_REMISE_CODE = "yvs_com_grille_remise";
    public static final String TABLE_TRANCHE_PLAN_TARIFAIRE_NAME = "Tranche Plan Tarifaire";
    public static final String TABLE_TRANCHE_PLAN_TARIFAIRE_CODE = "yvs_base_plan_tarifaire_tranche";
    public static final String TABLE_POINT_LIVRAISON_NAME = "Points de Livraison";
    public static final String TABLE_POINT_LIVRAISON_CODE = "yvs_base_point_livraison";
    public static final String TABLE_CRENEAU_DEPOT_NAME = "Points de Livraison";
    public static final String TABLE_CRENEAU_DEPOT_CODE = "yvs_com_creneau_depot";
    public static final String TABLE_CRENEAU_HORAIRE_USER_NAME = "Creneau Horaire Users";
    public static final String TABLE_CRENEAU_HORAIRE_USER_CODE = "yvs_com_creneau_horaire_users";
    public static final String TABLE_CRENEAU_POINT_NAME = "Creneau Point";
    public static final String TABLE_CRENEAU_POINT_CODE = "yvs_com_creneau_point";
    public static final String TABLE_PLAN_DE_RISTOURNE_NAME = "Plan De Ristourne";
    public static final String TABLE_PLAN_DE_RISTOURNE_CODE = "yvs_com_plan_ristourne";
    public static final String TABLE_RISTOURNE_NAME = "Ristourne Article";
    public static final String TABLE_RISTOURNE_CODE = "yvs_com_ristourne";
    public static final String TABLE_GRILLE_RISTOURNE_NAME = "Grille Ristourne Article";
    public static final String TABLE_GRILLE_RISTOURNE_CODE = "yvs_com_grille_ristourne";
    public static final String TABLE_USER_AGENCE_NAME = "Users Agence";
    public static final String TABLE_USER_AGENCE_CODE = "yvs_users_agence";

    public static final String TABLE_CODE_ACCES_NAME = "Code Acces";
    public static final String TABLE_CODE_ACCES_CODE = "yvs_base_code_acces";
    public static final String TABLE_CODE_ACCES_USERS_NAME = "Code Acces User";
    public static final String TABLE_CODE_ACCES_USERS_CODE = "yvs_base_users_acces";
    public static final String TABLE_CAISSES_NAME = "Caisses";
    public static final String TABLE_CAISSES_CODE = "yvs_base_caisse";
    public static final String TABLE_CAISSES_L_NAME = "Liason Caisses";
    public static final String TABLE_CAISSES_L_CODE = "yvs_base_liaison_caisse";
    public static final String TABLE_PARAM_VENTE_NAME = "Parametres Vente";
    public static final String TABLE_PARAM_VENTE_CODE = "yvs_com_parametre_vente";
    public static final String TABLE_PARAM_COMMERCIALE_NAME = "Parametres Commerciale";
    public static final String TABLE_PARAM_COMMERCIALE_CODE = "yvs_com_parametre";
    public static final String TABLE_COMMERCIALE_NAME = "Commerciale";
    public static final String TABLE_COMMERCIALE_CODE = "yvs_com_comerciale";
    public static final String TABLE_COMMERCIALE_POINT_NAME = "Commerciale Point";
    public static final String TABLE_COMMERCIALE_POINT_CODE = "yvs_com_commercial_point";
    public static final String TABLE_POINT_VENTE_DEPOT_NAME = "Point De Vente Depot";
    public static final String TABLE_POINT_VENTE_DEPOT_CODE = "yvs_base_point_vente_depot";
    public static final String TABLE_EXERCICES_NAME = "Exercices";
    public static final String TABLE_EXERCICES_CODE = "yvs_base_exercice";
    public static final String TABLE_ARTICLE_DEPOT_NAME = "Article Depot";
    public static final String TABLE_ARTICLE_DEPOT_CODE = "yvs_base_article_depot";
    public static final String TABLE_CONDITIONNEMENT_NAME = "Conditionnement";
    public static final String TABLE_CONDITIONNEMENT_CODE = "yvs_base_conditionnement";
    public static final String TABLE_ARTICLE_POINT_NAME = "Articles Point De Vente";
    public static final String TABLE_ARTICLE_POINT_CODE = "yvs_base_article_point";
    public static final String TABLE_CONDITIONNEMENT_POINT_NAME = "Articles Point De Vente";
    public static final String TABLE_CONDITIONNEMENT_POINT_CODE = "yvs_base_conditionnement_point";
    public static final String TABLE_CAISSE_USERS_NAME = "Caisses Users";
    public static final String TABLE_CAISSE_USERS_CODE = "yvs_base_caisse_user";
    public static final String TABLE_TAXE_VENTE_NAME = "Taxes Vente";
    public static final String TABLE_TAXE_VENTE_CODE = "yvs_com_taxe_contenu_vente";

    //Données fonctionnelle
    public static final String TABLE_HEADER_DOC_NAME = "Entete Facture";
    public static final String TABLE_HEADER_DOC_CODE = "yvs_com_entete_doc_vente";
    public static final String TABLE_DOC_VENTE_NAME = "Facture Vente";
    public static final String TABLE_DOC_VENTE_CODE = "yvs_com_doc_ventes";
    public static final String TABLE_CONTENT_DOC_VENTE_NAME = "Contenus Facture Vente";
    public static final String TABLE_CONTENT_DOC_VENTE_CODE = "yvs_com_contenu_doc_vente";
    public static final String TABLE_COMMERCIALE_VENTE_NAME = "Commerciale Vente";
    public static final String TABLE_COMMERCIALE_VENTE_CODE = "yvs_com_commercial_vente";
    public static final String TABLE_ACOMPTE_CLIENT_NAME = "Acompte Client";
    public static final String TABLE_ACOMPTE_CLIENT_CODE = "yvs_compta_acompte_client";
    public static final String TABLE_NOTIF_ACOMPTE_CLIENT_NAME = "Notif Acompte Client";
    public static final String TABLE_NOTIF_ACOMPTE_CLIENT_CODE = "yvs_compta_notif_reglement_vente";
    public static final String TABLE_PIECE_CAISSE_VENTE_NAME = "Pieces Caisses Vente";
    public static final String TABLE_PIECE_CAISSE_VENTE_CODE = "yvs_compta_caisse_piece_vente";

    /**
     * CLES DES ENTREES DU FICHIER .properties*
     */
    public static final String KEY_REMOTE_DB_NAME = "dbname_r";
    public static final String KEY_LOCAL_DB_NAME = "dbname";
    public static final String KEY_LOCAL_PORT = "port";
    public static final String KEY_REMOTE_PORT = "port_r";
    public static final String KEY_LOCAL_USERS = "user";
    public static final String KEY_REMOTE_USERS = "user_r";
    public static final String KEY_REMOTE_HOST = "host_r";
    public static final String KEY_LOCAL_HOST = "host";
    public static final String KEY_WEB_HOST = "WEB_HOST";
    public static final String KEY_WEB_PORT = "WEB_PORT";
    public static final String KEY_LOCAL_PASSWORD = "password";
    public static final String KEY_REMOTE_PASSWORD = "password_r";
    public static final String KEY_REMOTE_SOCIETE = "REMOTE_SOCIETE";
    public static final String KEY_LOCAL_SOCIETE = "LOCAL_SOCIETE";
    public static final String KEY_LOCAL_AGENCE = "LOCAL_AGENCE";
    public static final String KEY_APPS_PORT = "PORTAPP";
    public static final String KEY_MODE = "MODE";
    public static final String KEY_ENVIRONNEMENT = "ENVIRONNEMENT";
    public static final String KEY_CLIENT_DIVERS = "CLIENT_DIVERS";
    public static final String KEY_MODE_REGLEMENT = "MODE_REGLEMENT";
    public static final String KEY_MODEL_REGLEMENT = "MODEL_REGLEMENT";
    public static final String KEY_VILLE = "VILLE";
    public static final String KEY_SECTEUR = "SECTEUR";
    public static final String KEY_PATH = "PATH";
    public static final String KEY_ORIENTATION_PRINT = "PRINT_ORIENTATION";
    public static final String KEY_TYPE_PRINT = "TYPE_PRINT";
    public static final String KEY_USE_PRINTER = "USE_PRINTER";
    public static final String KEY_USE_CODE_BARRE = "USE_CODE_BARRE";
    public static final String KEY_PAPER_WIDTH = "PAPER_WIDTH";
    public static final String KEY_PAPER_HEIGHT = "PAPER_HEIGHT";
    public static final String KEY_PAPER_M_LEFT = "PAPER_M_LEFT";
    public static final String KEY_PAPER_M_RIGHT = "PAPER_M_RIGHT";
    public static final String KEY_PAPER_M_TOP = "PAPER_M_TOP";
    public static final String KEY_PAPER_M_BOTOM = "PAPER_M_BOTOM";
    public static final String KEY_DATE_INIT = "DATE_INIT";

    public static final String KEY_ENCRYPT = "1023";

    public Constantes() {
        LISTEN_TABLE = new ArrayList<>();
        ALLENTITY_BASE.add(new TableList(TABLE_CAISSE_USERS_NAME, TABLE_CAISSE_USERS_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_ARTICLE_POINT_NAME, TABLE_ARTICLE_POINT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CONDITIONNEMENT_POINT_NAME, TABLE_CONDITIONNEMENT_POINT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_SOCIETE_NAME, TABLE_SOCIETE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_AGENCE_NAME, TABLE_AGENCE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_USERS_NAME, TABLE_USERS_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_ELEMENT_REFERENCE_NAME, TABLE_ELEMENT_REFERENCE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_MODELE_DE_REFERENCE_NAME, TABLE_MODELE_DE_REFERENCE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_UNITE_MESURE_NAME, TABLE_UNITE_MESURE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_FAMILLE_NAME, TABLE_FAMILLE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_GROUPE_ART_NAME, TABLE_GROUPE_ART_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CLASSE_STAT_NAME, TABLE_CLASSE_STAT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_ARTICLE_NAME, TABLE_ARTICLE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CODE_BARRE_NAME, TABLE_CODE_BARRE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_DESC_ART_NAME, TABLE_DESC_ART_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_TAXES_NAME, TABLE_TAXES_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CATEGORIE_COMPTABLE_NAME, TABLE_CATEGORIE_COMPTABLE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_ARTICLE_CATEGORIE_COMPTABLE_NAME, TABLE_ARTICLE_CATEGORIE_COMPTABLE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_ARTICLE_CATEGORIE_COMPTABLE_TAXE_NAME, TABLE_ARTICLE_CATEGORIE_COMPTABLE_TAXE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_TIERS_NAME, TABLE_TIERS_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_TELEPHONE_TIERS_NAME, TABLE_TELEPHONE_TIERS_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CLIENTS_NAME, TABLE_CLIENTS_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_POINT_DE_VENTE_NAME, TABLE_POINT_DE_VENTE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_MODELE_DE_REGLEMENT_NAME, TABLE_MODELE_DE_REGLEMENT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_DICTIONNAIRES_NAME, TABLE_DICTIONNAIRES_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_MODE_DE_REGLEMENT_NAME, TABLE_MODE_DE_REGLEMENT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CATEGORIE_CLIENT_NAME, TABLE_CATEGORIE_CLIENT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_DEPOT_NAME, TABLE_DEPOT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_PLAN_TARIFAIRE_NAME, TABLE_PLAN_TARIFAIRE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_PLAN_REMISE_NAME, TABLE_PLAN_REMISE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_GRILLE_PLAN_REMISE_NAME, TABLE_GRILLE_PLAN_REMISE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_TRANCHE_PLAN_TARIFAIRE_NAME, TABLE_TRANCHE_PLAN_TARIFAIRE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_POINT_LIVRAISON_NAME, TABLE_POINT_LIVRAISON_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CRENEAU_DEPOT_NAME, TABLE_CRENEAU_DEPOT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CRENEAU_HORAIRE_USER_NAME, TABLE_CRENEAU_HORAIRE_USER_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CRENEAU_POINT_NAME, TABLE_CRENEAU_POINT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_PLAN_DE_RISTOURNE_NAME, TABLE_PLAN_DE_RISTOURNE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_RISTOURNE_NAME, TABLE_RISTOURNE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_GRILLE_RISTOURNE_NAME, TABLE_GRILLE_RISTOURNE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_TRANCHE_HORAIRE_NAME, TABLE_TRANCHE_HORAIRE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CODE_ACCES_NAME, TABLE_CODE_ACCES_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CODE_ACCES_USERS_NAME, TABLE_CODE_ACCES_USERS_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CAISSES_NAME, TABLE_CAISSES_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CAISSES_L_NAME, TABLE_CAISSES_L_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_USER_AGENCE_NAME, TABLE_USER_AGENCE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_PARAM_VENTE_NAME, TABLE_PARAM_VENTE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_PARAM_COMMERCIALE_NAME, TABLE_PARAM_COMMERCIALE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_COMMERCIALE_NAME, TABLE_COMMERCIALE_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_COMMERCIALE_POINT_NAME, TABLE_COMMERCIALE_POINT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_EXERCICES_NAME, TABLE_EXERCICES_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_POINT_VENTE_DEPOT_NAME, TABLE_POINT_VENTE_DEPOT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_ARTICLE_DEPOT_NAME, TABLE_ARTICLE_DEPOT_CODE));
        ALLENTITY_BASE.add(new TableList(TABLE_CONDITIONNEMENT_NAME, TABLE_CONDITIONNEMENT_CODE));

        ALLENTITY_DATA.add(new TableList(TABLE_HEADER_DOC_NAME, TABLE_HEADER_DOC_CODE));
        ALLENTITY_DATA.add(new TableList(TABLE_USER_AGENCE_NAME, TABLE_USER_AGENCE_CODE));
        ALLENTITY_DATA.add(new TableList(TABLE_DOC_VENTE_NAME, TABLE_DOC_VENTE_CODE));
        ALLENTITY_DATA.add(new TableList(TABLE_CONTENT_DOC_VENTE_NAME, TABLE_CONTENT_DOC_VENTE_CODE));
        ALLENTITY_DATA.add(new TableList(TABLE_COMMERCIALE_VENTE_NAME, TABLE_COMMERCIALE_VENTE_CODE));
        ALLENTITY_DATA.add(new TableList(TABLE_ACOMPTE_CLIENT_NAME, TABLE_ACOMPTE_CLIENT_CODE));
        ALLENTITY_DATA.add(new TableList(TABLE_NOTIF_ACOMPTE_CLIENT_NAME, TABLE_NOTIF_ACOMPTE_CLIENT_CODE));
        ALLENTITY_DATA.add(new TableList(TABLE_PIECE_CAISSE_VENTE_NAME, TABLE_PIECE_CAISSE_VENTE_CODE));

        LISTEN_TABLE.add(TABLE_CONDITIONNEMENT_CODE);
        LISTEN_TABLE.add(TABLE_CONDITIONNEMENT_POINT_CODE);
        LISTEN_TABLE.add(TABLE_ARTICLE_POINT_CODE);
        LISTEN_TABLE.add(TABLE_ARTICLE_CODE);
        LISTEN_TABLE.add(TABLE_CRENEAU_POINT_CODE);
        LISTEN_TABLE.add(TABLE_CRENEAU_HORAIRE_USER_CODE);
        LISTEN_TABLE.add(TABLE_CRENEAU_HORAIRE_USER_CODE);
        LISTEN_TABLE.add(TABLE_CRENEAU_HORAIRE_USER_CODE);
        PARAM_TABLE = "'yvs_base_point_vente', 'yvs_com_creneau_horaire_users', 'yvs_com_creneau_point', 'yvs_base_categorie_client', 'yvs_com_client', "
                + "'yvs_base_plan_tarifaire', 'yvs_com_grille_remise', 'yvs_com_remise', 'yvs_com_ristourne', 'yvs_com_grille_ristourne', 'yvs_com_plan_ristourne', 'yvs_base_conditionnement_point', "
                + "'yvs_base_articles', 'yvs_base_article_point'";
    }

    public static List<TableList> initialiseList() {
        return ALLENTITY_BASE;
    }

    public static final String DEFAULT_LOGIN = "ADMIN";
    public static final String PASSWORD = "YVS1910/%";

    public static String PASSWORD() {
        Calendar c = Calendar.getInstance();
        String r = PASSWORD;
        int v = c.get(Calendar.DAY_OF_MONTH);
        r += v > 9 ? v : "0" + v;
        v = c.get(Calendar.MONTH) + 1;
        r += v > 9 ? v : "0" + v;
        v = c.get(Calendar.YEAR);
        r += v > 9 ? v : "0" + v;
        return r;
    }

    public static Date givePrevOrNextDate(Date d, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static Date givePrevOrNextDate(Date d, int type, int nombre) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(type, nombre);
        return cal.getTime();
    }

    public static Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }

    public static Date localDateToDate(LocalDate ld) {
        Date date = new Date();
        if (ld != null) {
            date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return date;
    }

    public static int getParamDate(Date date, char param) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        switch (param) {
            case 'Y':
                return cal.get(Calendar.YEAR);
            case 'M':
                return cal.get(Calendar.MONTH) + 1;
            case 'D':
                return cal.get(Calendar.DAY_OF_MONTH);
            case 'h':
                return cal.get(Calendar.HOUR_OF_DAY);
            case 'm':
                return cal.get(Calendar.MINUTE);
            case 's':
                return cal.get(Calendar.SECOND);
        }
        return 0;
    }

    public static boolean isNumbrePair(int nombre) {
        return nombre % 2 == 0;
    }

    public static String getEntityFolder() {
        String source = new File("").getAbsolutePath();
        return new StringBuilder(source).append(SEPARATOR).append("src").append(SEPARATOR).append("lymytz").append(SEPARATOR).append("dao").append(SEPARATOR).append("entity").toString();
    }

    public static boolean asString(String valeur) {
        return valeur != null ? valeur.trim().length() > 0 : false;
    }

    public static boolean asLong(Long valeur) {
        if (valeur != null ? valeur > 0 : false) {
            return true;
        }
        return false;
    }

    public static String getQueryListenData() {
        String query = "SELECT DISTINCT l.id, l.name_table,l.id_source,l.action_name,l.message,l.date_save  FROM yvs_synchro_listen_table l "
                + "LEFT JOIN yvs_synchro_data_synchro ds ON (ds.id_listen=l.id AND ds.serveur=?)"
                + "INNER JOIN yvs_users_agence ua ON ua.id=l.author "
                + "INNER JOIN yvs_agences a ON a.id=ua.agence "
                + "LEFT JOIN yvs_com_creneau_horaire_users cu ON (cu.id=l.id_source AND l.name_table='yvs_com_creneau_horaire_users' AND cu.creneau_point IS NOT NULL) "
                + " WHERE to_listen IS true AND name_table IN "
                + "('yvs_base_point_vente', 'yvs_com_creneau_horaire_users', 'yvs_com_creneau_point', "
                + "'yvs_base_categorie_client', 'yvs_com_client','yvs_base_plan_tarifaire', "
                + "'yvs_com_grille_remise', 'yvs_base_caisse','yvs_base_caisse_user', "
                + "'yvs_com_remise', 'yvs_com_ristourne', 'yvs_com_grille_ristourne',  "
                + "'yvs_com_plan_ristourne', 'yvs_base_conditionnement_point','yvs_base_article_code_barre', "
                + "'yvs_base_unite_mesure','yvs_base_articles', 'yvs_base_article_point', "
                + "'yvs_users','yvs_base_famille_article', 'yvs_base_groupes_article', "
                + "'yvs_base_article_description','yvs_base_categorie_comptable', 'yvs_base_article_categorie_comptable', "
                + "'yvs_base_article_categorie_comptable_taxe','yvs_base_element_reference', 'yvs_base_model_reglement', "
                + "'yvs_base_mode_reglement','yvs_base_modele_reference', 'yvs_base_taxes', "
                + "'yvs_base_tiers','yvs_base_tiers_telephone', 'yvs_dictionnaire', "
                + "'yvs_base_depots','yvs_base_plan_tarifaire_tranche', 'yvs_com_creneau_depot', "
                + "'yvs_com_creneau_point','yvs_users_agence', 'yvs_base_users_acces', "
                + "'yvs_base_code_acces','yvs_com_parametre_vente', 'yvs_com_parametre', "
                + "'yvs_base_point_vente_depot','yvs_com_rabais', 'yvs_base_liaison_caisse', "
                + "'yvs_base_article_depot','yvs_base_conditionnement', 'yvs_base_article_point', "
                + "'yvs_base_exercice','yvs_base_conditionnement_point','yvs_base_caisse_user',"
                + "'yvs_agences','yvs_societes','yvs_grh_tranche_horaire') AND ds.id IS NULL AND a.societe=? "
                + "AND l.date_save> ?::date ORDER BY l.id"
                + " LIMIT 1 ";
        return query;
    }

    public static String getQueryListenDataCount() {
        String query = "SELECT COUNT(l.id),l.action_name  FROM yvs_synchro_listen_table l "
                + "LEFT JOIN yvs_synchro_data_synchro ds ON (ds.id_listen=l.id AND ds.serveur=?)"
                + "INNER JOIN yvs_users_agence ua ON ua.id=l.author "
                + "INNER JOIN yvs_agences a ON a.id=ua.agence "
                + "LEFT JOIN yvs_com_creneau_horaire_users cu ON (cu.id=l.id_source AND l.name_table='yvs_com_creneau_horaire_users' AND cu.creneau_point IS NOT NULL) "
                + " WHERE to_listen IS true AND name_table IN "
                + "('yvs_base_point_vente', 'yvs_com_creneau_horaire_users', 'yvs_com_creneau_point', "
                + "'yvs_base_categorie_client', 'yvs_com_client','yvs_base_plan_tarifaire', "
                + "'yvs_com_grille_remise', 'yvs_base_caisse','yvs_base_caisse_user', "
                + "'yvs_com_remise', 'yvs_com_ristourne', 'yvs_com_grille_ristourne',  "
                + "'yvs_com_plan_ristourne', 'yvs_base_conditionnement_point','yvs_base_article_code_barre', "
                + "'yvs_base_unite_mesure','yvs_base_articles', 'yvs_base_article_point', "
                + "'yvs_users','yvs_base_famille_article', 'yvs_base_groupes_article', "
                + "'yvs_base_article_description','yvs_base_categorie_comptable', 'yvs_base_article_categorie_comptable', "
                + "'yvs_base_article_categorie_comptable_taxe','yvs_base_element_reference', 'yvs_base_model_reglement', "
                + "'yvs_base_mode_reglement','yvs_base_modele_reference', 'yvs_base_taxes', "
                + "'yvs_base_tiers','yvs_base_tiers_telephone', 'yvs_dictionnaire', "
                + "'yvs_base_depots','yvs_base_plan_tarifaire_tranche', 'yvs_com_creneau_depot', "
                + "'yvs_com_creneau_point','yvs_users_agence', 'yvs_base_users_acces', "
                + "'yvs_base_code_acces','yvs_com_parametre_vente', 'yvs_com_parametre', "
                + "'yvs_base_point_vente_depot','yvs_com_rabais', 'yvs_base_liaison_caisse', "
                + "'yvs_base_article_depot','yvs_base_conditionnement', 'yvs_base_article_point', "
                + "'yvs_base_exercice','yvs_base_conditionnement_point','yvs_base_caisse_user',"
                + "'yvs_agences','yvs_societes','yvs_grh_tranche_horaire') AND ds.id IS NULL AND a.societe=? "
                + "AND l.date_save> ?::date "
                + "GROUP BY l.action_name";
        return query;
    }

    public static String TABLES_TO_SYNCHRO = "'yvs_com_entete_doc_vente','yvs_com_doc_ventes','yvs_com_contenu_doc_vente',"
            + "'yvs_compta_acompte_client','yvs_compta_notif_reglement_vente', "
            + "'yvs_compta_caisse_piece_vente'";

    public static String getQueryListenDelFacture() {
        String query = "SELECT DISTINCT l.id, l.name_table,l.id_source,l.action_name,l.message,l.date_save  "
                + "FROM yvs_synchro_listen_table l "
                + "LEFT JOIN yvs_synchro_data_synchro ds ON (ds.id_listen=l.id AND ds.serveur=?) "
                + "INNER JOIN yvs_users_agence ua ON ua.id=l.author "
                + "INNER JOIN yvs_agences a ON a.id=ua.agence "
                + "WHERE name_table IN ('yvs_com_doc_ventes') AND a.id=? "
                + "AND l.action_name='DELETE' AND l.date_save> ?::date ORDER BY l.id"
                + " LIMIT 1 ";
        return query;
    }

}
