--
-- PostgreSQL database dump
--

-- Dumped from database version 14.13 (Ubuntu 14.13-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.13 (Ubuntu 14.13-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: arrondi(bigint, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.arrondi(societe_ bigint, valeur_ double precision) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    _unite_ INT DEFAULT 0;
    _valeur_ DOUBLE PRECISION DEFAULT 0;
    _valeur_string_ CHARACTER VARYING DEFAULT '0';
    _params_ RECORD;
BEGIN
    SELECT INTO _params_ y.* FROM yvs_compta_parametre y WHERE y.societe = societe_;
    IF(_params_.id IS NOT NULL AND _params_.id > 0)THEN
        IF(_params_.decimal_arrondi IS TRUE)THEN
            _valeur_ = round(valeur_::numeric, _params_.valeur_arrondi);
        ELSE
            valeur_ = round(valeur_::numeric);
            IF(_params_.mode_arrondi IS NOT NULL)THEN
                _valeur_string_ = cast((valeur_::numeric) AS CHARACTER VARYING);
                _unite_ = CAST((SUBSTRING(_valeur_string_, char_length(_valeur_string_), char_length(_valeur_string_))) AS INT);
                CASE _params_.mode_arrondi
                    WHEN 'I' THEN
                        IF (_unite_ < _params_.multiple_arrondi) THEN
                            _valeur_ = valeur_ - _unite_;
                        ELSE
                            _valeur_ = valeur_ - (_unite_ - _params_.multiple_arrondi);
                        END IF;
                    WHEN 'S' THEN
                        IF (_unite_ < _params_.multiple_arrondi) THEN
                            _valeur_ = valeur_ + (_params_.multiple_arrondi - _unite_);
                        ELSE
                            _valeur_ = valeur_ + ((_params_.multiple_arrondi * 2) - _unite_);
                        END IF;
                    ELSE
                        IF (_unite_ < _params_.multiple_arrondi) THEN
                            IF (_unite_ < (_params_.multiple_arrondi / 2)) THEN
                                _valeur_ = valeur_ - _unite_;
                            ELSE
                                _valeur_ = valeur_ + (_params_.multiple_arrondi - _unite_);
                            END IF;
                        ELSE
                            IF (_unite_ < (_params_.multiple_arrondi + (_params_.multiple_arrondi / 2))) THEN
                                _valeur_ = valeur_ - (_unite_ - _params_.multiple_arrondi);
                            ELSE
                                _valeur_ = valeur_ + ((_params_.multiple_arrondi * 2) - _unite_);
                            END IF;
                        END IF;
                    END CASE;
            ELSE
                _valeur_ = valeur_;
            END IF;
        END IF;
    ELSE
        _valeur_ = round(valeur_::numeric, 0);
    END IF;
    RETURN _valeur_;
END;$$;


ALTER FUNCTION public.arrondi(societe_ bigint, valeur_ double precision) OWNER TO postgres;

--
-- Name: com_get_versement_attendu(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.com_get_versement_attendu(header_ character varying) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    entete_ RECORD;
BEGIN
    SELECT INTO entete_ u.users, e.date_entete FROM yvs_com_entete_doc_vente e INNER JOIN yvs_com_creneau_horaire_users u ON e.creneau = u.id WHERE e.id::character varying = header_;
    RETURN (SELECT com_get_versement_attendu(entete_.users, entete_.date_entete, entete_.date_entete));
END;$$;


ALTER FUNCTION public.com_get_versement_attendu(header_ character varying) OWNER TO postgres;

--
-- Name: com_get_versement_attendu(bigint, date, date); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.com_get_versement_attendu(users_ bigint, date_debut_ date, date_fin_ date) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    ca_ DOUBLE PRECISION DEFAULT 0;
    avance_ DOUBLE PRECISION DEFAULT 0;
    cs_p DOUBLE PRECISION DEFAULT 0;
BEGIN
    -- Recupere le montant TTC du contenu de la facture
    SELECT INTO ca_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
                                                                       INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
                                                                       INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
    WHERE h.users=users_ AND d.type_doc = 'FV' AND d.statut = 'V' AND d.document_lie IS NULL
      AND e.date_entete BETWEEN date_debut_ AND date_fin_;

    -- Recupere le total des couts de service supplementaire d'une facture
-- 	SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id
-- 																	  INNER JOIN yvs_com_doc_ventes d ON o.doc_vente = d.id
-- 																	  INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
-- 																	  INNER JOIN yvs_com_creneau_horaire_users h ON e.creneau = h.id
-- 									WHERE h.users=users_ AND d.type_doc = 'FV' AND d.statut = 'V' AND t.augmentation IS TRUE
-- 														 AND e.date_entete BETWEEN date_debut_ AND date_fin_;
-- 	ca_ = COALESCE(ca_, 0) + COALESCE(cs_p, 0);
--
    -- Evalue les avances sur commandes des factures des headers
    SELECT INTO avance_ SUM(p.montant) FROM yvs_compta_caisse_piece_vente p INNER JOIN yvs_com_doc_ventes d ON p.vente = d.id
    WHERE (d.type_doc = 'BCV' OR (d.type_doc = 'FV' AND d.document_lie IS NOT NULL)) AND p.caissier=users_
      AND p.date_paiement BETWEEN date_debut_ AND date_fin_;
    -- END LOOP;
    RETURN COALESCE(ca_, 0) + COALESCE(avance_, 0);
END;$$;


ALTER FUNCTION public.com_get_versement_attendu(users_ bigint, date_debut_ date, date_fin_ date) OWNER TO postgres;

--
-- Name: equilibre_vente_livre(bigint, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.equilibre_vente_livre(id_ bigint, by_parent_ boolean) RETURNS boolean
    LANGUAGE plpgsql
AS $$
DECLARE
    ch_ bigint default 0;
    line_ record;
    contenu_ record;
    qte_ double precision default 0;
    correct boolean default true;
    encours boolean default false;
    in_ boolean default false;

    query_control character varying;
    query_content character varying;

BEGIN
    -- Equilibre de l'etat reglé
    SELECT INTO line_ d.type_doc FROM yvs_com_doc_ventes d WHERE d.id = id_;
    IF(line_.type_doc = 'FV' OR line_.type_doc= 'BCV') THEN
        -- Equilibre de l'etat livré
        query_control = 'select sum(c.quantite) from yvs_com_contenu_doc_vente c inner join yvs_com_doc_ventes d on c.doc_vente = d.id
			where d.type_doc = ''BLV'' and d.statut = ''V'' and d.document_lie = '||id_;
        IF(by_parent_)THEN
            query_content = 'select id, conditionnement as unite, quantite::decimal as qte from yvs_com_contenu_doc_vente where doc_vente = '||id_;
        ELSE
            query_content = 'select article as id, conditionnement as unite, sum(quantite)::decimal as qte from yvs_com_contenu_doc_vente where doc_vente = '||id_||' group by article, conditionnement ORDER BY article, conditionnement';
        END IF;
        for contenu_ in execute query_content
            loop
                in_ = true;
                IF(by_parent_)THEN
                    execute query_control || ' and c.parent = '||COALESCE(contenu_.id, 0) into qte_;
                ELSE
                    execute query_control || ' and c.conditionnement = '||COALESCE(contenu_.unite, 0) into qte_;
                END IF;
                qte_ = coalesce(qte_, 0);
                if(qte_ < contenu_.qte)then
                    correct = false;
                    if(qte_ > 0)then
                        encours = true;
                        exit;
                    end if;
                end if;
            end loop;
        -- Bonus
        IF(by_parent_)THEN
            query_content = 'select id, conditionnement_bonus as unite, coalesce(quantite_bonus, 0)::decimal as qte from yvs_com_contenu_doc_vente where doc_vente = '||id_||' and coalesce(quantite_bonus, 0) > 0';
        ELSE
            query_content = 'select article_bonus as id, conditionnement_bonus as unite, sum(quantite_bonus)::decimal as qte from yvs_com_contenu_doc_vente where doc_vente = '||id_||' and coalesce(quantite_bonus, 0) > 0 group by article_bonus, conditionnement_bonus';
        END IF;
        for contenu_ in execute query_content
            loop
                in_ = true;
                IF(by_parent_)THEN
                    execute query_control || ' and c.parent = '||COALESCE(contenu_.id, 0) into qte_;
                ELSE
                    execute query_control || ' and c.conditionnement = '||COALESCE(contenu_.unite, 0) into qte_;
                END IF;
                qte_ = coalesce(qte_, 0);
                if(qte_ < contenu_.qte)then
                    correct = false;
                    if(qte_ > 0)then
                        encours = true;
                        exit;
                    end if;
                end if;
            end loop;

        if(in_)then
            if(correct)then
                update yvs_com_doc_ventes set statut_livre = 'L' where id = id_;
            else
                if(encours)then
                    update yvs_com_doc_ventes set statut_livre = 'R' where id = id_;
                elsif(by_parent_)then
                    update yvs_com_doc_ventes set statut_livre = 'W' where id = id_;
                end if;
                IF(by_parent_)then
                    PERFORM equilibre_vente_livre(id_, false);
                end if;
            end if;
        else
            update yvs_com_doc_ventes set statut_regle = 'W', statut_livre = 'W' where id = id_;
        end if;
        update yvs_workflow_valid_facture_vente set date_update = date_update where facture_vente = id_;
    END IF;
    return true;
END;$$;


ALTER FUNCTION public.equilibre_vente_livre(id_ bigint, by_parent_ boolean) OWNER TO postgres;

--
-- Name: FUNCTION equilibre_vente_livre(id_ bigint, by_parent_ boolean); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.equilibre_vente_livre(id_ bigint, by_parent_ boolean) IS 'equilibre l''etat livré des documents de vente';


--
-- Name: equilibre_vente_regle(bigint, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.equilibre_vente_regle(id_ bigint, by_parent_ boolean) RETURNS boolean
    LANGUAGE plpgsql
AS $$
DECLARE
    ttc_ double precision default 0;
    av_ double precision default 0;
    ch_ bigint default 0;
    line_ record;
    contenu_ record;
    qte_ double precision default 0;
    correct boolean default true;
    encours boolean default false;
    in_ boolean default false;

    query_control character varying;
    query_content character varying;

BEGIN
    -- Equilibre de l'etat reglé
    SELECT INTO line_ a.societe, d.type_doc FROM yvs_com_doc_ventes d INNER JOIN yvs_com_entete_doc_vente e ON d.entete_doc = e.id
                                                                      INNER JOIN yvs_com_creneau_horaire_users u ON e.creneau = u.id INNER JOIN yvs_users s on s.id = u.users INNER JOIN yvs_agences a ON s.agence = a.id WHERE d.id = id_;
    IF(line_.type_doc='FV' OR line_.type_doc='BCV') THEN
        ttc_ = (select get_ttc_vente(id_));
        ttc_ = arrondi(line_.societe, ttc_);
        SELECT INTO av_ SUM(coalesce(montant,0)) FROM yvs_compta_caisse_piece_vente WHERE vente = id_ AND statut_piece = 'P';
        IF(av_ IS NULL)THEN
            av_ = 0;
        END IF;
        av_ = arrondi(line_.societe, av_);
        if(coalesce(ttc_, 0) > 0)then
            select into ch_ count(y.id) FROM yvs_compta_caisse_piece_vente y INNER JOIN yvs_base_mode_reglement m on y.model = m.id
            WHERE y.vente = id_ and m.type_reglement = 'BANQUE';
            if(av_>=ttc_)then
                update yvs_com_doc_ventes set statut_regle = 'P' where id = id_;
            elsif (av_ > 0 or ch_ > 0) then
                update yvs_com_doc_ventes set statut_regle = 'R' where id = id_;
            else
                update yvs_com_doc_ventes set statut_regle = 'W' where id = id_;
            end if;
        else
            update yvs_com_doc_ventes set statut_regle = 'W', statut_livre = 'W' where id = id_;
        end if;
        --update yvs_workflow_valid_facture_vente set date_update = date_update where facture_vente = id_;
    END IF;
    return true;
END;$$;


ALTER FUNCTION public.equilibre_vente_regle(id_ bigint, by_parent_ boolean) OWNER TO postgres;

--
-- Name: FUNCTION equilibre_vente_regle(id_ bigint, by_parent_ boolean); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.equilibre_vente_regle(id_ bigint, by_parent_ boolean) IS 'equilibre l''etat reglé et l''etat livré des documents de vente';


--
-- Name: fusion_data_for_table(character varying, bigint, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.fusion_data_for_table(table_ character varying, new_value bigint, old_value bigint) RETURNS boolean
    LANGUAGE plpgsql
AS $$
DECLARE

BEGIN
    return fusion_data_for_table(table_, new_value, old_value::character varying);
END
$$;


ALTER FUNCTION public.fusion_data_for_table(table_ character varying, new_value bigint, old_value bigint) OWNER TO postgres;

--
-- Name: fusion_data_for_table(character varying, bigint, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.fusion_data_for_table(table_ character varying, new_value bigint, old_value character varying) RETURNS boolean
    LANGUAGE plpgsql
AS $$
DECLARE
    result_ boolean default false;
BEGIN
    if(table_ = 'yvs_base_conditionnement')then
        ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey;
    elsif(table_ = 'yvs_grh_tranche_horaire')then
        ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_tranche_fkey;
    elsif(table_ = 'yvs_niveau_acces')then
        DELETE FROM yvs_autorisation_module WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val);
        DELETE FROM yvs_autorisation_page_module WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val);
        DELETE FROM yvs_autorisation_ressources_page WHERE niveau_acces::character varying in (select val from regexp_split_to_table(old_value,',') val);
    elsif(table_ = 'yvs_base_articles')then
        DELETE FROM yvs_base_article_categorie_comptable WHERE COALESCE(article, 0) > 0 AND article::character varying in (select val from regexp_split_to_table(old_value,',') val);
        ALTER TABLE yvs_base_mouvement_stock DROP CONSTRAINT yvs_base_mouvement_stock_article_fkey;
    end if;

    result_ =  fusion_data_for_table_all(table_, new_value, old_value);

    if(table_ = 'yvs_base_conditionnement')then
        UPDATE yvs_base_mouvement_stock SET conditionnement = new_value WHERE conditionnement::character varying in (select val from regexp_split_to_table(old_value,',') val);
        ALTER TABLE yvs_base_mouvement_stock
            ADD CONSTRAINT yvs_base_mouvement_stock_conditionnement_fkey FOREIGN KEY (conditionnement)
                REFERENCES yvs_base_conditionnement (id) MATCH SIMPLE
                ON UPDATE CASCADE ON DELETE NO ACTION;
    elsif(table_ = 'yvs_base_articles')then
        UPDATE yvs_base_mouvement_stock SET article = new_value WHERE article::character varying in (select val from regexp_split_to_table(old_value,',') val);
        ALTER TABLE yvs_base_mouvement_stock
            ADD CONSTRAINT yvs_base_mouvement_stock_article_fkey FOREIGN KEY (article)
                REFERENCES yvs_base_articles (id) MATCH SIMPLE
                ON UPDATE CASCADE ON DELETE NO ACTION;
    elsif(table_ = 'yvs_grh_tranche_horaire')then
        UPDATE yvs_base_mouvement_stock SET tranche = new_value WHERE tranche::character varying in (select val from regexp_split_to_table(old_value,',') val);
        ALTER TABLE yvs_base_mouvement_stock
            ADD CONSTRAINT yvs_base_mouvement_stock_tranche_fkey FOREIGN KEY (tranche)
                REFERENCES yvs_grh_tranche_horaire (id) MATCH SIMPLE
                ON UPDATE CASCADE ON DELETE NO ACTION;
    elsif(table_ = 'yvs_base_tiers')then
        UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'TIERS';
    elsif(table_ = 'yvs_base_fournisseur')then
        UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'FOURNISSEUR';
    elsif(table_ = 'yvs_com_client')then
        UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'CLIENT';
    elsif(table_ = 'yvs_grh_employes')then
        UPDATE yvs_compta_content_journal SET compte_tiers = new_value WHERE compte_tiers::character varying in (select val from regexp_split_to_table(old_value,',') val) AND COALESCE(compte_tiers, 0) > 0 AND table_tiers = 'EMPLOYE';
    end if;
    return result_;
END
$$;


ALTER FUNCTION public.fusion_data_for_table(table_ character varying, new_value bigint, old_value character varying) OWNER TO postgres;

--
-- Name: fusion_data_for_table_all(character varying, bigint, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.fusion_data_for_table_all(table_ character varying, new_value bigint, old_value character varying) RETURNS boolean
    LANGUAGE plpgsql
AS $$
DECLARE
    table_name_ character varying;
    ids_ character varying default '0';
    constraint_ record;
    query_ character varying;

BEGIN
    -- Construction de la chaine des old_value
    if(old_value is not null and old_value not in ('', ' '))then
        for query_ in select val from regexp_split_to_table(old_value,',') val
            loop
                ids_ = ids_ || ','||query_;
            end loop;
    end if;
    -- Recherche de toutes les tables rattachées a la table actuell
    for table_name_ in select tablename from pg_tables where tablename not like 'pg_%' and schemaname = 'public' order by tablename
        loop
            -- Recherche de la clé secondaire liée a la clé primaire donnée
            FOR constraint_ IN SELECT k.CONSTRAINT_NAME, k.TABLE_NAME, k.COLUMN_NAME, f.TABLE_NAME AS TABLE_NAME_, f.COLUMN_NAME AS COLUMN_NAME_
                               FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k
                                        INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS c ON k.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND k.CONSTRAINT_NAME = c.CONSTRAINT_NAME
                                        INNER JOIN INFORMATION_SCHEMA.constraint_column_usage f ON f.CONSTRAINT_SCHEMA = c.CONSTRAINT_SCHEMA AND f.CONSTRAINT_NAME = c.CONSTRAINT_NAME
                               WHERE k.table_schema = 'public' AND k.TABLE_NAME = table_name_ AND c.CONSTRAINT_TYPE = 'FOREIGN KEY' AND f.TABLE_NAME = table_
                LOOP
                    -- Modification de l'ancienne valeur par la nouvelle
                    query_ = 'UPDATE public.'||table_name_||' SET '||constraint_.COLUMN_NAME||' = '||new_value||' WHERE '||constraint_.COLUMN_NAME||' in ('||ids_||')';
                    EXECUTE query_;
                    RAISE NOTICE 'query_ %',query_;
                END LOOP;
        end loop;
    EXECUTE 'DELETE FROM '||table_||' WHERE id  in ('||ids_||')';
    RAISE NOTICE '%','';
    return true;
END
$$;


ALTER FUNCTION public.fusion_data_for_table_all(table_ character varying, new_value bigint, old_value character varying) OWNER TO postgres;

--
-- Name: get_ca_entete_vente(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_ca_entete_vente(id_ bigint) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    total_ double precision default 0;

    avoir_ double precision default 0;
    cs_p double precision default 0;
    cs_ double precision default 0;

    remise_ double precision default 0;
    data_ record;

    header_ record;
    qte_ double precision;

BEGIN
    -- Recupere le montant TTC du contenu de la facture
    SELECT INTO total_ SUM(c.prix_total - c.ristourne) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes d ON c.doc_vente = d.id
    WHERE d.entete_doc = id_ AND d.type_doc = 'FV' AND d.statut='V';
    -- Recupere le total des couts de service supplementaire d'une facture
-- 	SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id
-- 																	  INNER JOIN yvs_com_doc_ventes d ON o.doc_vente = d.id
-- 									WHERE d.entete_doc = id_ AND d.type_doc = 'FV' AND t.augmentation IS TRUE AND o.service = TRUE;
-- 	SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id
-- 																							INNER JOIN yvs_com_doc_ventes fa ON da.document_lie = fa.id
-- 																							INNER JOIN yvs_com_entete_doc_vente e ON fa.entete_doc = e.id
-- 															WHERE da.type_doc = 'FAV' AND da.statut = 'V' AND e.id=id_;
    RETURN COALESCE(total_,0) + COALESCE(cs_p,0) -COALESCE(avoir_,0);
END;$$;


ALTER FUNCTION public.get_ca_entete_vente(id_ bigint) OWNER TO postgres;

--
-- Name: FUNCTION get_ca_entete_vente(id_ bigint); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_ca_entete_vente(id_ bigint) IS 'retourne le chiffre d''affaire d''un entete vente';


--
-- Name: get_pr(bigint, bigint, bigint, bigint, date, bigint, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_pr(agence_ bigint, article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    _depot_ bigint ;
    _agence bigint DEFAULT NULL;
    pr_ double precision;
    coef_ double precision;
    ecart_ double precision;
    prix_nom_ double precision;
    line_ record;
    line_ad_ record;
    categorie_ character varying;
    valorise_from_of_ Boolean;
    query_ character varying default 'SELECT m.cout_stock, a.categorie, a.taux_ecart_pr, m.conditionnement FROM yvs_base_mouvement_stock m INNER JOIN yvs_base_depots d ON m.depot = d.id
																						  INNER JOIN yvs_base_articles a ON a.id=m.article
														  WHERE COALESCE(m.calcul_pr, TRUE) IS TRUE AND m.mouvement = ''E''';

BEGIN
    SELECT INTO _depot_ depot_pr FROM yvs_base_article_depot WHERE article = article_ AND depot = depot_;
    IF(COALESCE(_depot_,0)<=0) THEN
        IF(COALESCE(depot_,0)>0) THEN
            -- Récupère l'agence du dépôt
            SELECT INTO _agence d.agence FROM yvs_base_depots d WHERE d.id=depot_;
            agence_=_agence;
        END IF;
        IF(COALESCE(agence_,0)<=0) THEN
            SELECT INTO line_ad_ depot, categorie FROM yvs_base_article_depot WHERE article = article_ AND default_pr IS TRUE  LIMIT 1;
        ELSE
            SELECT INTO line_ad_ depot, categorie FROM yvs_base_article_depot ad INNER JOIN yvs_base_depots d ON d.id=ad.depot
            WHERE article = article_ AND default_pr IS TRUE AND d.agence=agence_ LIMIT 1;
        END IF;
        _depot_=line_ad_.depot;
        categorie_=line_ad_.categorie;
    ELSE
        -- Récupère la catégorie du produit dans le dépôt
        SELECT INTO categorie_ ad.categorie FROM yvs_base_article_depot ad WHERE ad.article=article_ AND ad.depot=_depot_;
    END IF;
    depot_ = COALESCE(_depot_, depot_);
    IF(COALESCE(agence_,0)<=0) THEN
        SELECT INTO agence_ d.agence FROM yvs_base_depots d WHERE d.id=depot_;
    END IF;
    SELECT INTO valorise_from_of_ valorise_from_of FROM yvs_prod_parametre WHERE societe=(SELECT a.societe FROM yvs_agences a WHERE a.id=agence_);
    valorise_from_of_=COALESCE(valorise_from_of_,true);
    query_ = query_ || ' AND m.article = '||COALESCE(article_, 0)||' AND m.date_doc <= '||QUOTE_LITERAL(COALESCE(date_, CURRENT_DATE));
    IF(COALESCE(_depot_, 0) > 0)THEN
        query_ = query_ || ' AND m.depot = '||_depot_;
    END IF;
    IF(COALESCE(tranche_, 0) > 0)THEN
        query_ = query_ || ' AND m.tranche = '||tranche_;
    END IF;
    IF(COALESCE(current_, 0) > 0)THEN
        query_ = query_ || ' AND m.id != '||current_;
    END IF;
    query_ = query_ || ' ORDER BY m.date_doc DESC LIMIT 1';
    EXECUTE query_ INTO line_;
    ecart_= COALESCE(line_.taux_ecart_pr,0);
    categorie_=COALESCE(categorie_,line_.categorie);
    RAISE NOTICE 'Catégorie %',categorie_;
    IF((categorie_='PF' OR categorie_='PSF') AND COALESCE(valorise_from_of_,false) IS FALSE) THEN
        -- Retourne le prix de la nomenclature si la société evalue ses PF au PR
        pr_ = get_prix_nomenclature(article_, unite_, depot_, date_);
    ELSE
        IF(line_.conditionnement=unite_) THEN
            pr_=COALESCE(line_.cout_stock,0);
        ELSE
            --recherche le lien de conversion entre line_.conditionnement et unite_
            SELECT INTO coef_ taux_change FROM yvs_base_table_conversion t INNER JOIN yvs_base_conditionnement us ON us.unite=t.unite
                                                                           INNER JOIN yvs_base_conditionnement ud ON ud.unite=t.unite_equivalent
            WHERE us.id=unite_ AND ud.id=line_.conditionnement;
            coef_=COALESCE(coef_,0);
            pr_=COALESCE(line_.cout_stock,0)*coef_;
            RAISE NOTICE 'here % %',line_.conditionnement, line_.cout_stock;
        END IF;
        --RAISE NOTICE 'coût : % catégorie :%',line_.cout_stock, line_.categorie;
        IF(ecart_>0) THEN
            IF(categorie_='PF' OR categorie_='PSF') THEN
                prix_nom_ = get_prix_nomenclature(article_, unite_, depot_, date_);
                IF(COALESCE(prix_nom_,0)>0 AND (abs(pr_- prix_nom_))>ecart_) THEN
                    pr_=prix_nom_;
                END IF;
            END IF;
        END IF;
        IF(pr_ <=0)THEN
            IF(line_.categorie IS NULL) THEN
                SELECT INTO categorie_ a.categorie FROM yvs_base_article_depot a WHERE a.article=article_;
                IF(categorie_ IS NULL) THEN
                    SELECT INTO categorie_ a.categorie FROM yvs_base_articles a WHERE a.id=article_;
                END IF;
            ELSE
                categorie_=line_.categorie;
            END IF;
            IF(categorie_='PF' OR categorie_='PSF') THEN
                pr_ = get_prix_nomenclature(article_, unite_, depot_, date_);
                IF(COALESCE(pr_,0)<=0) THEN
                    pr_ = get_pua(article_, 0, 0, unite_);
                END IF;
            ELSE
                pr_ = get_pua(article_, 0, 0, unite_);
            END IF;
        END IF;
    END IF;
    RETURN pr_;
END;$$;


ALTER FUNCTION public.get_pr(agence_ bigint, article_ bigint, depot_ bigint, tranche_ bigint, date_ date, unite_ bigint, current_ bigint) OWNER TO postgres;

--
-- Name: get_puv(bigint, double precision, double precision, bigint, bigint, bigint, date, bigint, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_puv(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, depot_ bigint, point_ bigint, date_ date, unite_ bigint, min_ boolean) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    puv_ double precision;
    garde_ double precision;
    data_ record;
    tarif_ record;
    valeur_ double precision default 0;
    pr_  double precision default 0;

BEGIN
    valeur_ = qte_ * prix_;
    --pr_ = (select get_pr(article_, depot_, 0 ,date_, unite_));

    -- Recherche du prix du point de vente
    if(point_ is not null and point_ > 0)then
        if(unite_ is not null and unite_ > 0)then
            select into tarif_ y.*, a.prioritaire from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.id = unite_ and a.actif is true limit 1;
            if(tarif_.id is not null)then
                if(tarif_.prioritaire is true)then --Verification si ce prix est prioritaire
                    if(min_)then
                        if(tarif_.nature_prix_min = 'TAUX')then
                            puv_ = (pr_ * tarif_.prix_min) /100;
                        else
                            puv_ = tarif_.prix_min;
                        end if;
                    else
                        RAISE NOTICE 'Prix du point First ';
                        puv_ = tarif_.puv;
                    end if;
                end if;
            end if;
        end if;
    end if;

    if(puv_ is null or puv_ < 1)then
        -- Recherche du prix en fonction de la catégorie tarifaire
        if(client_ is not null and client_ > 0)then
            --Récupère les catégories du client
            for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
                loop
                    if(data_.permanent is false)then
                        if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
                            if(unite_ is not null and unite_ > 0)then
                                select into tarif_ y.* from yvs_base_plan_tarifaire y inner join yvs_base_conditionnement c on y.conditionnement = c.id where y.categorie = data_.categorie and y.article = article_ and c.id = unite_ and y.actif is true limit 1;
                                if(tarif_.id is not null)then
                                    if(min_)then
                                        if(tarif_.nature_prix_min = 'TAUX')then
                                            puv_ = (pr_ * tarif_.puv_min) /100;
                                        else
                                            puv_ = tarif_.puv_min;
                                        end if;
                                    else
                                        garde_ = tarif_.puv;
                                        select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
                                        if(tarif_.id IS NOT NULL)then
                                            puv_ = tarif_.puv;
                                        end if;
                                        if(puv_ is null or puv_ < 1)then
                                            puv_ = garde_;
                                        end if;
                                        RAISE NOTICE 'Prix catégoriel  périodique';
                                    end if;
                                end if;
                            end if;
                            exit;
                        end if;
                    else
                        if(unite_ is not null and unite_ > 0)then
                            select into tarif_ y.* from yvs_base_plan_tarifaire y inner join yvs_base_conditionnement c on y.conditionnement = c.id where y.categorie = data_.categorie and y.article = article_ and c.id = unite_ and y.actif is true limit 1;
                            if(tarif_.id is not null)then
                                if(min_)then
                                    if(tarif_.nature_prix_min = 'TAUX')then
                                        puv_ = (pr_ * tarif_.puv_min) /100;
                                    else
                                        puv_ = tarif_.puv_min;
                                    end if;
                                else
                                    garde_ = tarif_.puv;
                                    select into tarif_ * from yvs_base_plan_tarifaire_tranche WHERE plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
                                    if(tarif_.id IS NOT NULL)then
                                        puv_ = tarif_.puv;
                                    end if;
                                    if(puv_ is null or puv_ < 1)then
                                        puv_ = garde_;
                                    end if;
                                    RAISE NOTICE 'Prix catégoriel  permanent';
                                end if;
                            end if;
                        end if;
                        exit;
                    end if;
                end loop;
        end if;

        if(puv_ is null or puv_ < 1)then
            -- Recherche du prix du point de vente non prioritaire
            if(point_ is not null and point_ > 0)then
                if(unite_ is not null and unite_ > 0)then
                    select into tarif_ y.* from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.id = unite_ and a.actif is true limit 1;
                    if(tarif_.id is not null)then
                        if(min_)then
                            if(tarif_.nature_prix_min = 'TAUX')then
                                puv_ = (pr_ * tarif_.prix_min) /100;
                            else
                                puv_ = tarif_.prix_min;
                            end if;
                        else
                            RAISE NOTICE 'Prix point seconds';
                            puv_ = tarif_.puv;
                        end if;
                    end if;
                end if;
            end if;

            if(puv_ is null or puv_ < 1)then
                -- Recherche du prix de l'article sur la fiche d'article
                if(unite_ is not null and unite_ > 0)then
                    select into tarif_ * from yvs_base_conditionnement where id = unite_;
                    if(tarif_.id is not null)then
                        if(min_)then
                            if(tarif_.nature_prix_min = 'TAUX')then
                                puv_ = (pr_ * tarif_.prix_min) /100;
                            else
                                puv_ = tarif_.prix_min;
                            end if;
                        else
                            RAISE NOTICE 'Prixcond';
                            puv_ = tarif_.prix;
                        end if;
                    end if;
                end if;
            end if;
        end if;
    end if;
    if(puv_ is null or puv_ <1)then
        puv_ = 0;
    end if;
    return puv_;
END;$$;


ALTER FUNCTION public.get_puv(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, depot_ bigint, point_ bigint, date_ date, unite_ bigint, min_ boolean) OWNER TO postgres;

--
-- Name: FUNCTION get_puv(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, depot_ bigint, point_ bigint, date_ date, unite_ bigint, min_ boolean); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_puv(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, depot_ bigint, point_ bigint, date_ date, unite_ bigint, min_ boolean) IS 'retourne le prix de vente d'' article';


--
-- Name: get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ integer) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    data_ record;
    tarif_ record;

    valeur_ double precision default 0;
    remise_ double precision;
    garde_ double precision;

    famille_ bigint;

    control_ boolean default false;
    planifier_ boolean default false;
BEGIN
    valeur_ = qte_ * prix_;
    if(client_ is not null and client_ > 0)then
        select into famille_ y.famille from yvs_base_articles y where y.id = article_;
        for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
            loop
                control_ = false;
                planifier_ = false;
-- 			RAISE NOTICE 'categorie : %',data_.categorie;
                if(data_.permanent is false)then
                    if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
                        control_ = true;
                        planifier_ = true;
                    end if;
                else
                    control_ = true;
                end if;
-- 			RAISE NOTICE 'control_ : %',control_;
                if(control_)then
                    if(unite_ is not null and unite_ > 0)then
                        select into tarif_ y.* from yvs_base_plan_tarifaire y where y.categorie = data_.categorie and y.actif is true and ((y.article is not null and (y.article = article_ and y.conditionnement = (select c.id from yvs_base_conditionnement c where c.article = y.article and c.unite = unite_))) or (y.article is null and y.famille = famille_)) order by y.article limit 1;
                        if(tarif_.id is not null)then
                            if(tarif_.nature_remise = 'TAUX')then
                                garde_ = valeur_ * (tarif_.remise /100);
                            else
                                garde_ = qte_ * tarif_.remise;
                            end if;
-- 						RAISE NOTICE 'garde_ : %',garde_;
                            select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
                            if(tarif_.id IS NOT NULL) then
-- 							RAISE NOTICE 'tarif_.id : %',tarif_.id;
                                if(tarif_.nature_remise = 'TAUX')then
                                    remise_ = valeur_ * (tarif_.remise /100);
                                else
                                    remise_ = qte_ * tarif_.remise;
                                end if;
                            end if;
                            if(remise_ is null or remise_ <= 0)then
                                remise_ = garde_;
                            end if;
                            exit;
                        end if;
                    else
                        select into tarif_ y.* from yvs_base_plan_tarifaire y where y.categorie = data_.categorie and y.actif is true and ((y.article is not null and y.article = article_) or (y.article is null and y.famille = famille_)) order by y.article limit 1;
                        if(tarif_.id is not null)then
                            if(tarif_.nature_remise = 'TAUX')then
                                garde_ = valeur_ * (tarif_.remise /100);
                            else
                                garde_ = qte_ * tarif_.remise;
                            end if;
                            select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
                            if(tarif_.id IS NOT NULL) then
                                if(tarif_.nature_remise = 'TAUX')then
                                    remise_ = valeur_ * (tarif_.remise /100);
                                else
                                    remise_ = qte_ * tarif_.remise;
                                end if;
                            end if;
                            if(remise_ is null or remise_ <= 0)then
                                remise_ = garde_;
                            end if;
                            exit;
                        end if;
                    end if;
                    if(planifier_)then
                        exit;
                    end if;
                end if;
            end loop;
    end if;
    if(remise_ is null or remise_ <= 0)then
        if(unite_ is not null and unite_ > 0)then
            select into tarif_ y.* from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.unite = unite_ and a.actif is true limit 1;
            if(tarif_.id IS NOT NULL) then
                if(tarif_.nature_remise = 'TAUX')then
                    remise_ = valeur_ * (tarif_.remise /100);
                else
                    remise_ = qte_ * tarif_.remise;
                end if;
            end if;
        else
            select into tarif_ * from yvs_base_article_point where point = point_ and article = article_ and actif is true limit 1;
            if(tarif_.id IS NOT NULL) then
                if(tarif_.nature_remise = 'TAUX')then
                    remise_ = valeur_ * (tarif_.remise /100);
                else
                    remise_ = qte_ * tarif_.remise;
                end if;
            end if;
        end if;
        if(remise_ is null or remise_ <= 0)then
            if(unite_ is not null and unite_ > 0)then
                select into remise_ remise from yvs_base_conditionnement where article = article_ and unite = unite_;
                if(remise_ is not null) then
                    remise_ = valeur_ * (remise_/100);
                end if;
            else
                select into remise_ remise from yvs_base_articles where id = article_;
                if(remise_ is not null) then
                    remise_ = valeur_ * (remise_/100);
                end if;
            end if;
        end if;
    end if;
    if(remise_ is null or remise_ <=0)then
        remise_ = 0;
    end if;
    return remise_;
END;$$;


ALTER FUNCTION public.get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ integer) OWNER TO postgres;

--
-- Name: FUNCTION get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ integer); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ integer) IS 'retourne la remise sur vente d'' article';


--
-- Name: get_remise_vente(bigint, double precision, double precision, bigint, bigint, date, bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ bigint) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    data_ record;
    tarif_ record;

    valeur_ double precision default 0;
    remise_ double precision;
    garde_ double precision;

    famille_ bigint;

    control_ boolean default false;
    planifier_ boolean default false;
BEGIN
    valeur_ = qte_ * prix_;
    if(client_ is not null and client_ > 0)then
        select into famille_ y.famille from yvs_base_articles y where y.id = article_;
        for data_ in select * from yvs_com_categorie_tarifaire where client = client_ and actif is true order by priorite desc
            loop
                control_ = false;
                planifier_ = false;
-- 			RAISE NOTICE 'categorie : %',data_.categorie;
                if(data_.permanent is false)then
                    if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
                        control_ = true;
                        planifier_ = true;
                    end if;
                else
                    control_ = true;
                end if;
-- 			RAISE NOTICE 'control_ : %',control_;
                if(control_)then
                    if(unite_ is not null and unite_ > 0)then
                        select into tarif_ y.* from yvs_base_plan_tarifaire y where y.categorie = data_.categorie and y.actif is true and ((y.article is not null and (y.article = article_ and y.conditionnement = (select c.id from yvs_base_conditionnement c where c.article = y.article and c.unite = unite_))) or (y.article is null and y.famille = famille_)) order by y.article limit 1;
                        if(tarif_.id is not null)then
                            if(tarif_.nature_remise = 'TAUX')then
                                garde_ = valeur_ * (tarif_.remise /100);
                            else
                                garde_ = qte_ * tarif_.remise;
                            end if;
-- 						RAISE NOTICE 'garde_ : %',garde_;
                            select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
                            if(tarif_.id IS NOT NULL) then
-- 							RAISE NOTICE 'tarif_.id : %',tarif_.id;
                                if(tarif_.nature_remise = 'TAUX')then
                                    remise_ = valeur_ * (tarif_.remise /100);
                                else
                                    remise_ = qte_ * tarif_.remise;
                                end if;
                            end if;
                            if(remise_ is null or remise_ <= 0)then
                                remise_ = garde_;
                            end if;
                            exit;
                        end if;
                    else
                        select into tarif_ y.* from yvs_base_plan_tarifaire y where y.categorie = data_.categorie and y.actif is true and ((y.article is not null and y.article = article_) or (y.article is null and y.famille = famille_)) order by y.article limit 1;
                        if(tarif_.id is not null)then
                            if(tarif_.nature_remise = 'TAUX')then
                                garde_ = valeur_ * (tarif_.remise /100);
                            else
                                garde_ = qte_ * tarif_.remise;
                            end if;
                            select into tarif_ * from yvs_base_plan_tarifaire_tranche where plan = tarif_.id and ((base = 'QTE' and (qte_ between valeur_min and valeur_max)) or (base = 'CA' and (valeur_ between valeur_min and valeur_max)));
                            if(tarif_.id IS NOT NULL) then
                                if(tarif_.nature_remise = 'TAUX')then
                                    remise_ = valeur_ * (tarif_.remise /100);
                                else
                                    remise_ = qte_ * tarif_.remise;
                                end if;
                            end if;
                            if(remise_ is null or remise_ <= 0)then
                                remise_ = garde_;
                            end if;
                            exit;
                        end if;
                    end if;
                    if(planifier_)then
                        exit;
                    end if;
                end if;
            end loop;
    end if;
    if(remise_ is null or remise_ <= 0)then
        if(unite_ is not null and unite_ > 0)then
            select into tarif_ y.* from yvs_base_conditionnement_point y inner join yvs_base_article_point a on y.article = a.id inner join yvs_base_conditionnement c on y.conditionnement = c.id where a.point = point_ and a.article = article_ and c.unite = unite_ and a.actif is true limit 1;
            if(tarif_.id IS NOT NULL) then
                if(tarif_.nature_remise = 'TAUX')then
                    remise_ = valeur_ * (tarif_.remise /100);
                else
                    remise_ = qte_ * tarif_.remise;
                end if;
            end if;
        else
            select into tarif_ * from yvs_base_article_point where point = point_ and article = article_ and actif is true limit 1;
            if(tarif_.id IS NOT NULL) then
                if(tarif_.nature_remise = 'TAUX')then
                    remise_ = valeur_ * (tarif_.remise /100);
                else
                    remise_ = qte_ * tarif_.remise;
                end if;
            end if;
        end if;
        if(remise_ is null or remise_ <= 0)then
            if(unite_ is not null and unite_ > 0)then
                select into remise_ remise from yvs_base_conditionnement where article = article_ and unite = unite_;
                if(remise_ is not null) then
                    remise_ = valeur_ * (remise_/100);
                end if;
            else
                select into remise_ remise from yvs_base_articles where id = article_;
                if(remise_ is not null) then
                    remise_ = valeur_ * (remise_/100);
                end if;
            end if;
        end if;
    end if;
    if(remise_ is null or remise_ <=0)then
        remise_ = 0;
    end if;
    return remise_;
END;$$;


ALTER FUNCTION public.get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ bigint) OWNER TO postgres;

--
-- Name: FUNCTION get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ bigint); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_remise_vente(article_ bigint, qte_ double precision, prix_ double precision, client_ bigint, point_ bigint, date_ date, unite_ bigint) IS 'retourne la remise sur vente d'' article';


--
-- Name: get_ristourne(bigint, double precision, double precision, bigint, date); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_ristourne(cond_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    ristourne_ double precision;
    data_ record;
    plan_ bigint;
    tarif_ record;
    valeur_ double precision default 0;

    famille_ bigint;

    control_ boolean default false;
    planifier_ boolean default false;
BEGIN
    valeur_ = qte_ * prix_;
    if(client_ is not null and client_ > 0)then
        select into data_ * from yvs_com_client where id = client_;
        if(data_.plan_ristourne is not null)then

            select into plan_ id from yvs_com_plan_ristourne WHERE actif = true and id = data_.plan_ristourne;
            if(plan_ is not null)then
                RAISE NOTICE 'Here... %',plan_;
                select into famille_ y.famille from yvs_base_articles y inner join yvs_base_conditionnement c on c.article = y.id where c.id = cond_;
                for data_ in select * from yvs_com_ristourne WHERE actif = true and plan = plan_ AND ((conditionnement is not null and conditionnement = cond_) or (conditionnement is null and famille = famille_)) AND nature = 'R'
                    loop
                        control_ = false;
                        planifier_ = false;
                        if(data_.permanent is false)then
                            if(data_.date_debut <= date_ and date_ <= data_.date_fin)then
                                control_ = true;
                                planifier_ = true;
                            end if;
                        else
                            control_ = true;
                        end if;
                        if(control_)then
                            select into tarif_ * from yvs_com_grille_ristourne where ristourne = data_.id and ((base = 'QTE' and qte_ between montant_minimal and montant_maximal)or((base IN ('CATTC', 'CAHT')) and valeur_ between montant_minimal and montant_maximal));
                            if(tarif_.id is not null)then
                                if(tarif_.nature_montant = 'TAUX')then
                                    ristourne_ = valeur_ * (tarif_.montant_ristourne /100);
                                else
                                    ristourne_ = qte_ * tarif_.montant_ristourne;
                                end if;
                            end if;
                            exit;
                        end if;
                    end loop;
            end if;
        end if;
    end if;

    if(ristourne_ is null or ristourne_ <1)then
        ristourne_ = 0;
    end if;
    return ristourne_;
END;$$;


ALTER FUNCTION public.get_ristourne(cond_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date) OWNER TO postgres;

--
-- Name: FUNCTION get_ristourne(cond_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_ristourne(cond_ bigint, qte_ double precision, prix_ double precision, client_ bigint, date_ date) IS 'retourne la ristourne d'' article';


--
-- Name: get_taxe(bigint, bigint, bigint, double precision, double precision, double precision, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_taxe(article_ bigint, categorie_ bigint, compte_ bigint, remise_ double precision, qte_ double precision, prix_ double precision, is_vente_ boolean) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    art_ record;
    taxe_ double precision default 0;
    valeur_ double precision default 0;
    data_ record;
    id_ bigint;

BEGIN
    select into art_ * from yvs_base_articles where id = article_;

    -- Recherche de la categorie comptable taxable
    if(compte_ is not null and compte_ > 0)then
        select into id_ id from yvs_base_article_categorie_comptable where article = article_ and categorie = categorie_ and compte = compte_ and actif = true;
    else
        select into id_ id from yvs_base_article_categorie_comptable where article = article_ and categorie = categorie_ and actif = true;
    end if;

    -- Verification si la categorie comptable taxable existe
    if(id_ is not null and id_ > 0)then
        if(is_vente_)then
            -- Verification si le prix de vente est le prix TTC
            if(art_.puv_ttc)then
                -- Calcul de la taxe sur le prix de vente
                for data_ in select t.taux from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
                    loop
                        taxe_ = taxe_ + data_.taux;
                    end loop;
                -- On retire la taxe sur le prix de vente
                prix_ = prix_ / ( 1 + (taxe_ / 100));
            end if;
        else
            -- Verification si le prix d'achat est le prix TTC
            if(art_.pua_ttc)then
                -- Calcul de la taxe sur le prix d'achat
                for data_ in select t.taux from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
                    loop
                        taxe_ = taxe_ + ((data_.taux / 100 ) * (prix_ / (1 + (data_.taux / 100))));
                    end loop;
                -- On retire la taxe sur le prix d'achat
                prix_ = prix_ - taxe_;
            end if;
        end if;
        -- Calcul de la valeur
        valeur_ = qte_ * prix_;

        -- Calcul de la taxe sur la valeur
        for data_ in select c.app_remise , t.taux from yvs_base_article_categorie_comptable_taxe c inner join yvs_base_taxes t on c.taxe = t.id where c.article_categorie = id_ and c.actif = true
            loop
                -- Verification si la taxe s'applique sur la remise
                if(data_.app_remise)then
                    taxe_ = taxe_ + (((valeur_ - remise_) * data_.taux) / 100);
                else
                    taxe_ = taxe_ + ((valeur_ * data_.taux) / 100);
                end if;
            end loop;
    end if;
    if(taxe_ is null or taxe_ <1)then
        taxe_ = 0;
    end if;
    return taxe_;
END;$$;


ALTER FUNCTION public.get_taxe(article_ bigint, categorie_ bigint, compte_ bigint, remise_ double precision, qte_ double precision, prix_ double precision, is_vente_ boolean) OWNER TO postgres;

--
-- Name: FUNCTION get_taxe(article_ bigint, categorie_ bigint, compte_ bigint, remise_ double precision, qte_ double precision, prix_ double precision, is_vente_ boolean); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_taxe(article_ bigint, categorie_ bigint, compte_ bigint, remise_ double precision, qte_ double precision, prix_ double precision, is_vente_ boolean) IS 'retourne la taxe d'' article';


--
-- Name: get_ttc_vente(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_ttc_vente(id_ bigint) RETURNS double precision
    LANGUAGE plpgsql
AS $$
DECLARE
    total_ double precision default 0;
    cs_p double precision default 0;
    avoir_ double precision default 0;
BEGIN
    -- Recupere le montant TTC du contenu de la facture
    SELECT INTO total_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c WHERE c.doc_vente = id_;
    -- Recupere le total des couts de service supplementaire d'une facture
    --SELECT INTO cs_p SUM(o.montant) FROM yvs_com_cout_sup_doc_vente o INNER JOIN yvs_grh_type_cout t ON o.type_cout = t.id
    --WHERE o.doc_vente = id_ AND t.augmentation IS TRUE;
    SELECT INTO avoir_ SUM(c.prix_total) FROM yvs_com_contenu_doc_vente c INNER JOIN yvs_com_doc_ventes da ON c.doc_vente = da.id
                                                                          INNER JOIN yvs_com_doc_ventes fa ON da.document_lie = fa.id
    WHERE da.type_doc = 'FAV' AND da.statut = 'V' AND fa.id=id_;
    RETURN COALESCE(total_,0) + COALESCE(cs_p,0) -COALESCE(avoir_,0);
END;$$;


ALTER FUNCTION public.get_ttc_vente(id_ bigint) OWNER TO postgres;

--
-- Name: FUNCTION get_ttc_vente(id_ bigint); Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON FUNCTION public.get_ttc_vente(id_ bigint) IS 'retourne le montant TTC d''un doc vente';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: yvs_agences; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_agences (
                                    id bigint NOT NULL,
                                    abbreviation character varying(255),
                                    adresse character varying(255),
                                    codeagence character varying(255),
                                    designation character varying(255),
                                    region character varying(255),
                                    actif boolean DEFAULT true,
                                    email character varying,
                                    telephone character varying,
                                    adresse_ip character varying,
                                    code_postal character varying,
                                    date_save timestamp without time zone,
                                    date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                    societe bigint
);


ALTER TABLE public.yvs_agences OWNER TO postgres;

--
-- Name: yvs_agences_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_agences_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_agences_id_seq OWNER TO postgres;

--
-- Name: yvs_agences_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_agences_id_seq OWNED BY public.yvs_agences.id;


--
-- Name: yvs_base_article_categorie_comptable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_article_categorie_comptable (
                                                             id bigint NOT NULL,
                                                             article bigint,
                                                             categorie bigint,
                                                             actif boolean,
                                                             author bigint,
                                                             date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                             date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_base_article_categorie_comptable OWNER TO postgres;

--
-- Name: yvs_base_article_categorie_comptable_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_article_categorie_comptable_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_article_categorie_comptable_id_seq OWNER TO postgres;

--
-- Name: yvs_base_article_categorie_comptable_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_article_categorie_comptable_id_seq OWNED BY public.yvs_base_article_categorie_comptable.id;


--
-- Name: yvs_base_article_categorie_comptable_taxe; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_article_categorie_comptable_taxe (
                                                                  id bigint NOT NULL,
                                                                  taxe bigint,
                                                                  app_remise boolean,
                                                                  actif boolean,
                                                                  article_categorie bigint,
                                                                  author bigint,
                                                                  date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                                  date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_base_article_categorie_comptable_taxe OWNER TO postgres;

--
-- Name: yvs_base_article_categorie_comptable_taxe_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_article_categorie_comptable_taxe_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_article_categorie_comptable_taxe_id_seq OWNER TO postgres;

--
-- Name: yvs_base_article_categorie_comptable_taxe_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_article_categorie_comptable_taxe_id_seq OWNED BY public.yvs_base_article_categorie_comptable_taxe.id;


--
-- Name: yvs_base_article_code_barre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_article_code_barre (
                                                    id bigint NOT NULL,
                                                    code_barre character varying,
                                                    description character varying,
                                                    date_save timestamp without time zone,
                                                    date_update timestamp without time zone DEFAULT now(),
                                                    author bigint,
                                                    conditionnement bigint
);


ALTER TABLE public.yvs_base_article_code_barre OWNER TO postgres;

--
-- Name: yvs_base_article_code_barre_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_article_code_barre_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_article_code_barre_id_seq OWNER TO postgres;

--
-- Name: yvs_base_article_code_barre_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_article_code_barre_id_seq OWNED BY public.yvs_base_article_code_barre.id;


--
-- Name: yvs_base_article_depot; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_article_depot (
                                               id bigint NOT NULL,
                                               article bigint,
                                               depot bigint,
                                               stock_max double precision,
                                               stock_min double precision,
                                               quantite_stock double precision DEFAULT 0,
                                               actif boolean DEFAULT true,
                                               stock_alert double precision DEFAULT 0,
                                               author bigint,
                                               marg_stock_moyen double precision DEFAULT 0,
                                               stock_net double precision DEFAULT '-1'::integer,
                                               date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                               date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                               requiere_lot boolean DEFAULT false,
                                               suivi_stock boolean DEFAULT true,
                                               default_cond bigint,
                                               sell_without_stock boolean DEFAULT true,
                                               depot_pr bigint,
                                               default_pr boolean DEFAULT false,
                                               quantite_vendu double precision,
                                               quantite_achat double precision,
                                               quantite_produit double precision,
                                               categorie character varying
);


ALTER TABLE public.yvs_base_article_depot OWNER TO postgres;

--
-- Name: COLUMN yvs_base_article_depot.default_cond; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_base_article_depot.default_cond IS 'Conditionnement par défaut de l''article dans le dépôt';


--
-- Name: COLUMN yvs_base_article_depot.default_pr; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_base_article_depot.default_pr IS 'Propriété unite pour un article. défini quel dépôt nous sert de référence pour obtenir le PR dans les etats de synthèse et statistiques de marge';


--
-- Name: yvs_base_article_depot_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_article_depot_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_article_depot_id_seq OWNER TO postgres;

--
-- Name: yvs_base_article_depot_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_article_depot_id_seq OWNED BY public.yvs_base_article_depot.id;


--
-- Name: yvs_base_article_point; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_article_point (
                                               id bigint NOT NULL,
                                               article bigint,
                                               point bigint,
                                               change_prix boolean DEFAULT false,
                                               actif boolean DEFAULT true,
                                               author bigint,
                                               prioritaire boolean DEFAULT false,
                                               nature_prix_min character varying DEFAULT 'MONTANT'::character varying,
                                               remise double precision DEFAULT 0,
                                               nature_remise character varying DEFAULT 'MONTANT'::character varying,
                                               date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                               date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_base_article_point OWNER TO postgres;

--
-- Name: yvs_base_article_point_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_article_point_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_article_point_id_seq OWNER TO postgres;

--
-- Name: yvs_base_article_point_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_article_point_id_seq OWNED BY public.yvs_base_article_point.id;


--
-- Name: yvs_base_articles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_articles (
                                          id bigint NOT NULL,
                                          change_prix boolean,
                                          description character varying,
                                          designation character varying(255),
                                          photo_1 character varying,
                                          masse_net double precision,
                                          ref_art character varying(255),
                                          suivi_en_stock boolean,
                                          visible_en_synthese boolean,
                                          groupe bigint,
                                          coefficient double precision,
                                          service boolean,
                                          methode_val character varying,
                                          actif boolean DEFAULT true,
                                          photo_2 character varying,
                                          photo_3 character varying,
                                          categorie character varying,
                                          famille bigint,
                                          duree_vie double precision,
                                          duree_garantie double precision,
                                          fichier character varying,
                                          puv_ttc boolean DEFAULT false,
                                          pua_ttc boolean DEFAULT false,
                                          date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                          date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                          classe1 bigint,
                                          classe2 bigint,
                                          type_service character(1) DEFAULT 'C'::bpchar,
                                          date_last_mvt date,
                                          taux_ecart_pr double precision DEFAULT 0,
                                          execute_trigger character varying,
                                          tags character varying
);


ALTER TABLE public.yvs_base_articles OWNER TO postgres;

--
-- Name: yvs_base_articles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_articles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_articles_id_seq OWNER TO postgres;

--
-- Name: yvs_base_articles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_articles_id_seq OWNED BY public.yvs_base_articles.id;


--
-- Name: yvs_base_caisse; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_caisse (
                                        id bigint NOT NULL,
                                        intitule character varying,
                                        author bigint,
                                        adresse character varying,
                                        actif boolean,
                                        parent bigint,
                                        can_negative boolean DEFAULT false,
                                        mode_reg_defaut integer,
                                        type_caisse character varying,
                                        date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                        date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                        default_caisse boolean DEFAULT false,
                                        caissier bigint,
                                        give_billetage boolean DEFAULT false,
                                        code character varying,
                                        principal boolean,
                                        code_acces bigint,
                                        execute_trigger character varying
);


ALTER TABLE public.yvs_base_caisse OWNER TO postgres;

--
-- Name: yvs_base_caisse_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_caisse_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_caisse_id_seq OWNER TO postgres;

--
-- Name: yvs_base_caisse_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_caisse_id_seq OWNED BY public.yvs_base_caisse.id;


--
-- Name: yvs_base_caisse_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_caisse_user (
                                             id bigint NOT NULL,
                                             id_user bigint,
                                             id_caisse bigint,
                                             author bigint,
                                             date_save timestamp without time zone,
                                             date_update timestamp without time zone,
                                             actif boolean DEFAULT true
);


ALTER TABLE public.yvs_base_caisse_user OWNER TO postgres;

--
-- Name: yvs_base_caisse_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_caisse_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_caisse_user_id_seq OWNER TO postgres;

--
-- Name: yvs_base_caisse_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_caisse_user_id_seq OWNED BY public.yvs_base_caisse_user.id;


--
-- Name: yvs_base_categorie_client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_categorie_client (
                                                  id bigint NOT NULL,
                                                  code character varying,
                                                  libelle character varying,
                                                  description character varying,
                                                  societe bigint,
                                                  parent bigint,
                                                  lier_client boolean DEFAULT false,
                                                  defaut boolean DEFAULT false,
                                                  author bigint,
                                                  actif boolean,
                                                  date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                  date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_base_categorie_client OWNER TO postgres;

--
-- Name: yvs_base_categorie_client_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_categorie_client_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_categorie_client_id_seq OWNER TO postgres;

--
-- Name: yvs_base_categorie_client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_categorie_client_id_seq OWNED BY public.yvs_base_categorie_client.id;


--
-- Name: yvs_base_categorie_comptable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_categorie_comptable (
                                                     id bigint NOT NULL,
                                                     code_appel character varying(255),
                                                     code character varying(255),
                                                     nature character varying(255),
                                                     societe bigint,
                                                     designation character varying,
                                                     actif boolean DEFAULT true,
                                                     author bigint,
                                                     date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                     date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_base_categorie_comptable OWNER TO postgres;

--
-- Name: yvs_base_categorie_comptable_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_categorie_comptable_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_categorie_comptable_id_seq OWNER TO postgres;

--
-- Name: yvs_base_categorie_comptable_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_categorie_comptable_id_seq OWNED BY public.yvs_base_categorie_comptable.id;


--
-- Name: yvs_base_classes_stat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_classes_stat (
                                              id bigint NOT NULL,
                                              code_ref character varying,
                                              designation character varying,
                                              actif boolean,
                                              visible_synthese boolean,
                                              visible_journal boolean,
                                              author bigint,
                                              date_save timestamp without time zone,
                                              date_update timestamp without time zone,
                                              societe bigint,
                                              parent bigint
);


ALTER TABLE public.yvs_base_classes_stat OWNER TO postgres;

--
-- Name: yvs_base_classes_stat_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_classes_stat_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_classes_stat_id_seq OWNER TO postgres;

--
-- Name: yvs_base_classes_stat_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_classes_stat_id_seq OWNED BY public.yvs_base_classes_stat.id;


--
-- Name: yvs_base_code_acces; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_code_acces (
                                            id bigint NOT NULL,
                                            code character varying,
                                            description character varying,
                                            date_save timestamp without time zone DEFAULT now(),
                                            date_update timestamp without time zone DEFAULT now(),
                                            author bigint,
                                            societe bigint
);


ALTER TABLE public.yvs_base_code_acces OWNER TO postgres;

--
-- Name: yvs_base_code_acces_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_code_acces_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_code_acces_id_seq OWNER TO postgres;

--
-- Name: yvs_base_code_acces_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_code_acces_id_seq OWNED BY public.yvs_base_code_acces.id;


--
-- Name: yvs_base_conditionnement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_conditionnement (
                                                 id bigint NOT NULL,
                                                 article bigint,
                                                 unite bigint,
                                                 author bigint,
                                                 prix double precision DEFAULT 0,
                                                 prix_min double precision DEFAULT 0,
                                                 nature_prix_min character varying DEFAULT 'MONTANT'::character varying,
                                                 remise double precision DEFAULT 0,
                                                 cond_vente boolean,
                                                 date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                 date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                 prix_achat double precision DEFAULT 0,
                                                 photo character varying,
                                                 by_achat boolean DEFAULT false,
                                                 by_prod boolean DEFAULT false,
                                                 defaut boolean DEFAULT true,
                                                 prix_prod double precision,
                                                 marge_min double precision DEFAULT 0,
                                                 actif boolean
);


ALTER TABLE public.yvs_base_conditionnement OWNER TO postgres;

--
-- Name: yvs_base_conditionnement_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_conditionnement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_conditionnement_id_seq OWNER TO postgres;

--
-- Name: yvs_base_conditionnement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_conditionnement_id_seq OWNED BY public.yvs_base_conditionnement.id;


--
-- Name: yvs_base_conditionnement_point; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_conditionnement_point (
                                                       id bigint NOT NULL,
                                                       conditionnement bigint,
                                                       article bigint,
                                                       puv double precision DEFAULT 0,
                                                       prix_min double precision DEFAULT 0,
                                                       nature_prix_min character varying DEFAULT 'MONTANT'::character varying,
                                                       remise double precision DEFAULT 0,
                                                       nature_remise character varying DEFAULT 'MONTANT'::character varying,
                                                       author bigint,
                                                       date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                       date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                       avance_commance double precision DEFAULT 0,
                                                       actif boolean DEFAULT true,
                                                       change_prix boolean DEFAULT false
);


ALTER TABLE public.yvs_base_conditionnement_point OWNER TO postgres;

--
-- Name: yvs_base_conditionnement_point_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_conditionnement_point_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_conditionnement_point_id_seq OWNER TO postgres;

--
-- Name: yvs_base_conditionnement_point_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_conditionnement_point_id_seq OWNED BY public.yvs_base_conditionnement_point.id;


--
-- Name: yvs_base_depots; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_depots (
                                        id bigint NOT NULL,
                                        abbreviation character varying(255),
                                        adresse character varying(255),
                                        code character varying(255),
                                        control_stock boolean,
                                        crenau boolean,
                                        designation character varying(255),
                                        op_achat boolean,
                                        op_production boolean,
                                        op_transit boolean,
                                        op_vente boolean,
                                        agence bigint,
                                        actif boolean DEFAULT true,
                                        description character varying,
                                        op_technique boolean DEFAULT false,
                                        op_retour boolean,
                                        op_reserv boolean,
                                        date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                        date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                        principal boolean DEFAULT true,
                                        type_pf boolean,
                                        type_ne boolean,
                                        type_psf boolean,
                                        type_mp boolean,
                                        verify_appro boolean
);


ALTER TABLE public.yvs_base_depots OWNER TO postgres;

--
-- Name: COLUMN yvs_base_depots.crenau; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_base_depots.crenau IS 'spécifie si les opérations effectuées dans le dépôt exige un crénaux horaire';


--
-- Name: yvs_base_depots_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_depots_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_depots_id_seq OWNER TO postgres;

--
-- Name: yvs_base_depots_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_depots_id_seq OWNED BY public.yvs_base_depots.id;


--
-- Name: yvs_base_element_reference; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_element_reference (
                                                   id bigint NOT NULL,
                                                   designation character varying,
                                                   module character varying,
                                                   date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                   date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                   model_courant boolean DEFAULT false,
                                                   default_prefix character varying
);


ALTER TABLE public.yvs_base_element_reference OWNER TO postgres;

--
-- Name: yvs_base_element_reference_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_element_reference_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_element_reference_id_seq OWNER TO postgres;

--
-- Name: yvs_base_element_reference_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_element_reference_id_seq OWNED BY public.yvs_base_element_reference.id;


--
-- Name: yvs_base_exercice; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_exercice (
                                          id bigint NOT NULL,
                                          reference character varying,
                                          date_debut date,
                                          date_fin date,
                                          actif boolean DEFAULT false,
                                          societe bigint,
                                          author bigint,
                                          cloturer boolean,
                                          date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                          date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                          execute_trigger character varying
);


ALTER TABLE public.yvs_base_exercice OWNER TO postgres;

--
-- Name: yvs_base_exercice_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_exercice_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_exercice_id_seq OWNER TO postgres;

--
-- Name: yvs_base_exercice_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_exercice_id_seq OWNED BY public.yvs_base_exercice.id;


--
-- Name: yvs_base_famille_article; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_famille_article (
                                                 id bigint NOT NULL,
                                                 reference_famille character varying,
                                                 designation character varying,
                                                 description character varying,
                                                 famille_parent bigint,
                                                 societe bigint,
                                                 author bigint,
                                                 actif boolean DEFAULT true,
                                                 date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                 date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                 prefixe character varying
);


ALTER TABLE public.yvs_base_famille_article OWNER TO postgres;

--
-- Name: yvs_base_famille_article_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_famille_article_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_famille_article_id_seq OWNER TO postgres;

--
-- Name: yvs_base_famille_article_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_famille_article_id_seq OWNED BY public.yvs_base_famille_article.id;


--
-- Name: yvs_base_groupes_article; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_groupes_article (
                                                 id bigint NOT NULL,
                                                 description character varying(255),
                                                 refgroupe character varying(255),
                                                 code_appel character varying,
                                                 actif boolean DEFAULT true,
                                                 groupe_parent bigint,
                                                 designation character varying,
                                                 author bigint,
                                                 date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                 date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                 societe bigint
);


ALTER TABLE public.yvs_base_groupes_article OWNER TO postgres;

--
-- Name: yvs_base_groupes_article_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_groupes_article_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_groupes_article_id_seq OWNER TO postgres;

--
-- Name: yvs_base_groupes_article_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_groupes_article_id_seq OWNED BY public.yvs_base_groupes_article.id;


--
-- Name: yvs_base_liaison_caisse; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_liaison_caisse (
                                                id bigint NOT NULL,
                                                caisse_source bigint,
                                                caisse_cible bigint,
                                                actif boolean DEFAULT true,
                                                date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_base_liaison_caisse OWNER TO postgres;

--
-- Name: yvs_base_liaison_caisse_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_liaison_caisse_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_liaison_caisse_id_seq OWNER TO postgres;

--
-- Name: yvs_base_liaison_caisse_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_liaison_caisse_id_seq OWNED BY public.yvs_base_liaison_caisse.id;


--
-- Name: yvs_base_mode_reglement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_mode_reglement (
                                                id bigint NOT NULL,
                                                designation character varying(200),
                                                description character varying(500),
                                                societe bigint,
                                                actif boolean DEFAULT true,
                                                author bigint,
                                                type_reglement character varying,
                                                default_mode boolean DEFAULT false,
                                                date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                numero_marchand character varying,
                                                code_paiement character varying
);


ALTER TABLE public.yvs_base_mode_reglement OWNER TO postgres;

--
-- Name: yvs_base_mode_reglement_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_mode_reglement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_mode_reglement_id_seq OWNER TO postgres;

--
-- Name: yvs_base_mode_reglement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_mode_reglement_id_seq OWNED BY public.yvs_base_mode_reglement.id;


--
-- Name: yvs_base_model_reglement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_model_reglement (
                                                 id bigint NOT NULL,
                                                 reference character varying,
                                                 description character varying,
                                                 societe bigint,
                                                 actif boolean DEFAULT true,
                                                 author bigint,
                                                 type character(1) DEFAULT 'C'::bpchar,
                                                 date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                 date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_base_model_reglement OWNER TO postgres;

--
-- Name: yvs_base_model_reglement_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_model_reglement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_model_reglement_id_seq OWNER TO postgres;

--
-- Name: yvs_base_model_reglement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_model_reglement_id_seq OWNED BY public.yvs_base_model_reglement.id;


--
-- Name: yvs_base_modele_reference; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_modele_reference (
                                                  id bigint NOT NULL,
                                                  prefix character varying,
                                                  jour boolean,
                                                  mois boolean,
                                                  annee boolean,
                                                  taille integer,
                                                  separateur character(1),
                                                  element bigint,
                                                  societe bigint,
                                                  module character varying,
                                                  code_point boolean DEFAULT false,
                                                  longueur_code_point integer DEFAULT 0,
                                                  author bigint,
                                                  date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                  date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                  element_code character varying
);


ALTER TABLE public.yvs_base_modele_reference OWNER TO postgres;

--
-- Name: yvs_base_modele_reference_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_modele_reference_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_modele_reference_id_seq OWNER TO postgres;

--
-- Name: yvs_base_modele_reference_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_modele_reference_id_seq OWNED BY public.yvs_base_modele_reference.id;


--
-- Name: yvs_base_plan_tarifaire; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_plan_tarifaire (
                                                id bigint NOT NULL,
                                                categorie bigint,
                                                article bigint,
                                                puv double precision DEFAULT 0,
                                                remise double precision DEFAULT 0,
                                                coef_augmentation double precision DEFAULT 0,
                                                nature_coef_augmentation character varying DEFAULT 'TAUX'::character varying,
                                                ristourne double precision DEFAULT 0,
                                                nature_remise character varying DEFAULT 'TAUX'::character varying,
                                                nature_ristourne character varying DEFAULT 'TAUX'::character varying,
                                                puv_min double precision DEFAULT 0,
                                                nature_prix_min character varying DEFAULT 'MONTANT'::character varying,
                                                conditionnement bigint,
                                                date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                actif boolean DEFAULT true,
                                                author bigint,
                                                famille bigint
);


ALTER TABLE public.yvs_base_plan_tarifaire OWNER TO postgres;

--
-- Name: yvs_base_plan_tarifaire_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_plan_tarifaire_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_plan_tarifaire_id_seq OWNER TO postgres;

--
-- Name: yvs_base_plan_tarifaire_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_plan_tarifaire_id_seq OWNED BY public.yvs_base_plan_tarifaire.id;


--
-- Name: yvs_base_plan_tarifaire_tranche; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_plan_tarifaire_tranche (
                                                        id bigint NOT NULL,
                                                        base character varying DEFAULT 'QTE'::character varying,
                                                        valeur_min double precision DEFAULT 0,
                                                        valeur_max double precision DEFAULT 0,
                                                        remise double precision DEFAULT 0,
                                                        nature_remise character varying DEFAULT 'TAUX'::character varying,
                                                        plan bigint,
                                                        author bigint,
                                                        puv double precision DEFAULT 0,
                                                        date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                        date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_base_plan_tarifaire_tranche OWNER TO postgres;

--
-- Name: yvs_base_plan_tarifaire_tranche_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_plan_tarifaire_tranche_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_plan_tarifaire_tranche_id_seq OWNER TO postgres;

--
-- Name: yvs_base_plan_tarifaire_tranche_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_plan_tarifaire_tranche_id_seq OWNED BY public.yvs_base_plan_tarifaire_tranche.id;


--
-- Name: yvs_base_point_livraison; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_point_livraison (
                                                 id bigint NOT NULL,
                                                 libelle character varying,
                                                 ville bigint,
                                                 societe bigint,
                                                 date_save timestamp without time zone,
                                                 date_update timestamp without time zone,
                                                 author bigint,
                                                 execute_trigger character varying,
                                                 telephone character varying,
                                                 lieu_dit character varying,
                                                 description character varying
);


ALTER TABLE public.yvs_base_point_livraison OWNER TO postgres;

--
-- Name: yvs_base_point_livraison_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_point_livraison_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_point_livraison_id_seq OWNER TO postgres;

--
-- Name: yvs_base_point_livraison_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_point_livraison_id_seq OWNED BY public.yvs_base_point_livraison.id;


--
-- Name: yvs_base_point_vente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_point_vente (
                                             id bigint NOT NULL,
                                             code character varying,
                                             libelle character varying,
                                             adresse character varying,
                                             agence bigint,
                                             reglement_auto boolean DEFAULT false,
                                             actif boolean,
                                             livraison_on character(1) DEFAULT 'A'::bpchar,
                                             date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                             commission_for character(1) DEFAULT 'C'::bpchar,
                                             date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                             prix_min_strict boolean DEFAULT false,
                                             vente_online boolean DEFAULT false,
                                             accept_client_no_name boolean DEFAULT false,
                                             validation_reglement boolean DEFAULT false,
                                             telephone character varying,
                                             author bigint
);


ALTER TABLE public.yvs_base_point_vente OWNER TO postgres;

--
-- Name: COLUMN yvs_base_point_vente.commission_for; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_base_point_vente.commission_for IS '''C'' pour commercial
''P'' pout point de vente';


--
-- Name: yvs_base_point_vente_depot; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_point_vente_depot (
                                                   id bigint NOT NULL,
                                                   depot bigint NOT NULL,
                                                   point_vente bigint NOT NULL,
                                                   actif boolean DEFAULT false,
                                                   author bigint,
                                                   principal boolean DEFAULT false,
                                                   date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                   date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                   execute_trigger character varying
);


ALTER TABLE public.yvs_base_point_vente_depot OWNER TO postgres;

--
-- Name: yvs_base_point_vente_depot_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_point_vente_depot_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_point_vente_depot_id_seq OWNER TO postgres;

--
-- Name: yvs_base_point_vente_depot_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_point_vente_depot_id_seq OWNED BY public.yvs_base_point_vente_depot.id;


--
-- Name: yvs_base_point_vente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_point_vente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_point_vente_id_seq OWNER TO postgres;

--
-- Name: yvs_base_point_vente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_point_vente_id_seq OWNED BY public.yvs_base_point_vente.id;


--
-- Name: yvs_base_taxes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_taxes (
                                       id bigint NOT NULL,
                                       code_taxe character varying(255),
                                       taux double precision,
                                       code_appel character varying,
                                       designation character varying,
                                       actif boolean DEFAULT true,
                                       societe bigint,
                                       author bigint,
                                       date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                       date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                       libelle_print character varying(255)
);


ALTER TABLE public.yvs_base_taxes OWNER TO postgres;

--
-- Name: yvs_base_taxes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_taxes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_taxes_id_seq OWNER TO postgres;

--
-- Name: yvs_base_taxes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_taxes_id_seq OWNED BY public.yvs_base_taxes.id;


--
-- Name: yvs_base_tiers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_tiers (
                                       id bigint NOT NULL,
                                       adresse character varying(255),
                                       bp character varying(255),
                                       civilite character varying(255),
                                       classe character varying(255),
                                       client boolean DEFAULT false,
                                       code_barre character varying(255),
                                       code_tiers character varying(255),
                                       compte character varying(255),
                                       email character varying(255),
                                       fournisseur boolean DEFAULT false,
                                       logo character varying(255),
                                       nom character varying(255),
                                       point_de_vente character varying(255),
                                       prenom character varying(255),
                                       representant boolean DEFAULT false,
                                       tel character varying(255),
                                       code_postal character varying,
                                       statut character varying,
                                       always_visible boolean,
                                       actif boolean DEFAULT true,
                                       ville bigint,
                                       site character varying,
                                       societe bigint,
                                       st_societe boolean,
                                       secteur bigint,
                                       responsable character varying,
                                       employe boolean DEFAULT false,
                                       date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                       date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                       personnel boolean DEFAULT false
);


ALTER TABLE public.yvs_base_tiers OWNER TO postgres;

--
-- Name: COLUMN yvs_base_tiers.personnel; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_base_tiers.personnel IS 'permet de spécifier les tiers qui peuvent benéficier d''une ration en PF';


--
-- Name: yvs_base_tiers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_tiers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_tiers_id_seq OWNER TO postgres;

--
-- Name: yvs_base_tiers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_tiers_id_seq OWNED BY public.yvs_base_tiers.id;


--
-- Name: yvs_base_unite_mesure; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_unite_mesure (
                                              id bigint NOT NULL,
                                              reference character varying,
                                              libelle character varying,
                                              societe bigint,
                                              description character varying,
                                              type character varying,
                                              author bigint,
                                              date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                              date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                              defaut boolean DEFAULT false
);


ALTER TABLE public.yvs_base_unite_mesure OWNER TO postgres;

--
-- Name: COLUMN yvs_base_unite_mesure.type; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_base_unite_mesure.type IS 'Q pour Quantité - T pour Temps - L pour Longueur';


--
-- Name: yvs_base_unite_mesure_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_unite_mesure_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_unite_mesure_id_seq OWNER TO postgres;

--
-- Name: yvs_base_unite_mesure_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_unite_mesure_id_seq OWNED BY public.yvs_base_unite_mesure.id;


--
-- Name: yvs_base_users_acces; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_base_users_acces (
                                             id bigint NOT NULL,
                                             code bigint,
                                             users bigint,
                                             date_save timestamp without time zone DEFAULT now(),
                                             date_update timestamp without time zone DEFAULT now(),
                                             author bigint,
                                             execute_trigger character varying
);


ALTER TABLE public.yvs_base_users_acces OWNER TO postgres;

--
-- Name: yvs_base_users_acces_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_base_users_acces_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_base_users_acces_id_seq OWNER TO postgres;

--
-- Name: yvs_base_users_acces_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_base_users_acces_id_seq OWNED BY public.yvs_base_users_acces.id;


--
-- Name: yvs_com_categorie_tarifaire; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_categorie_tarifaire (
                                                    id bigint NOT NULL,
                                                    client bigint,
                                                    categorie bigint,
                                                    date_debut date,
                                                    date_fin date,
                                                    priorite integer,
                                                    actif boolean DEFAULT true,
                                                    permanent boolean DEFAULT true,
                                                    author bigint,
                                                    date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                    date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_com_categorie_tarifaire OWNER TO postgres;

--
-- Name: yvs_com_categorie_tarifaire_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_categorie_tarifaire_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_categorie_tarifaire_id_seq OWNER TO postgres;

--
-- Name: yvs_com_categorie_tarifaire_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_categorie_tarifaire_id_seq OWNED BY public.yvs_com_categorie_tarifaire.id;


--
-- Name: yvs_com_client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_client (
                                       id bigint NOT NULL,
                                       tiers bigint,
                                       categorie_comptable bigint,
                                       defaut boolean DEFAULT false,
                                       code_client character varying,
                                       actif boolean DEFAULT true,
                                       model bigint,
                                       nom character varying,
                                       prenom character varying,
                                       plan_ristourne bigint,
                                       suivi_comptable boolean DEFAULT false,
                                       seuil_solde double precision DEFAULT 0,
                                       create_by bigint,
                                       date_creation timestamp without time zone,
                                       date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                       confirmer boolean DEFAULT true,
                                       ligne bigint
);


ALTER TABLE public.yvs_com_client OWNER TO postgres;

--
-- Name: COLUMN yvs_com_client.ligne; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_com_client.ligne IS 'Ligne géographique';


--
-- Name: yvs_com_client_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_client_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_client_id_seq OWNER TO postgres;

--
-- Name: yvs_com_client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_client_id_seq OWNED BY public.yvs_com_client.id;


--
-- Name: yvs_com_comerciale; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_comerciale (
                                           id bigint NOT NULL,
                                           code_ref character varying,
                                           nom character varying,
                                           prenom character varying,
                                           telephone character varying,
                                           author bigint,
                                           date_save timestamp without time zone,
                                           date_update timestamp without time zone,
                                           utilisateur bigint,
                                           agence bigint,
                                           actif boolean DEFAULT false,
                                           tiers bigint,
                                           defaut boolean DEFAULT false
);


ALTER TABLE public.yvs_com_comerciale OWNER TO postgres;

--
-- Name: yvs_com_comerciale_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_comerciale_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_comerciale_id_seq OWNER TO postgres;

--
-- Name: yvs_com_comerciale_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_comerciale_id_seq OWNED BY public.yvs_com_comerciale.id;


--
-- Name: yvs_com_commercial_point; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_commercial_point (
                                                 id bigint NOT NULL,
                                                 commercial bigint,
                                                 point bigint,
                                                 date_save timestamp without time zone DEFAULT now(),
                                                 date_update timestamp without time zone DEFAULT now(),
                                                 author bigint,
                                                 execute_trigger character varying
);


ALTER TABLE public.yvs_com_commercial_point OWNER TO postgres;

--
-- Name: yvs_com_commercial_point_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_commercial_point_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_commercial_point_id_seq OWNER TO postgres;

--
-- Name: yvs_com_commercial_point_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_commercial_point_id_seq OWNED BY public.yvs_com_commercial_point.id;


--
-- Name: yvs_com_commercial_vente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_commercial_vente (
                                                 id bigint NOT NULL,
                                                 commercial bigint,
                                                 facture bigint,
                                                 taux double precision DEFAULT 0,
                                                 date_save timestamp without time zone DEFAULT now(),
                                                 date_update timestamp without time zone DEFAULT now(),
                                                 author bigint,
                                                 responsable boolean DEFAULT false,
                                                 diminue_ca boolean DEFAULT false
);


ALTER TABLE public.yvs_com_commercial_vente OWNER TO postgres;

--
-- Name: yvs_com_commercial_vente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_commercial_vente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_commercial_vente_id_seq OWNER TO postgres;

--
-- Name: yvs_com_commercial_vente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_commercial_vente_id_seq OWNED BY public.yvs_com_commercial_vente.id;


--
-- Name: yvs_com_contenu_doc_vente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_contenu_doc_vente (
                                                  id bigint NOT NULL,
                                                  article bigint,
                                                  doc_vente bigint,
                                                  quantite double precision,
                                                  prix double precision,
                                                  remise double precision,
                                                  taxe double precision,
                                                  ristourne double precision DEFAULT 0,
                                                  comission double precision,
                                                  supp boolean DEFAULT false,
                                                  actif boolean DEFAULT true,
                                                  date_contenu date DEFAULT ('now'::text)::date,
                                                  date_save timestamp without time zone DEFAULT now(),
                                                  author bigint,
                                                  commentaire character varying,
                                                  parent bigint,
                                                  num_serie character varying(255),
                                                  rabais double precision DEFAULT 0,
                                                  pr double precision DEFAULT 0,
                                                  statut character varying,
                                                  puv_min double precision DEFAULT 0,
                                                  quantite_bonus double precision DEFAULT 0,
                                                  id_reservation bigint,
                                                  prix_total double precision DEFAULT 0,
                                                  mouv_stock boolean DEFAULT false,
                                                  qualite bigint,
                                                  depot_livraison_prevu bigint,
                                                  statut_livree character(1) DEFAULT 'W'::bpchar,
                                                  taux_remise double precision DEFAULT 0,
                                                  conditionnement bigint,
                                                  date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                  article_bonus bigint,
                                                  conditionnement_bonus bigint,
                                                  calcul_pr boolean DEFAULT true
);


ALTER TABLE public.yvs_com_contenu_doc_vente OWNER TO postgres;

--
-- Name: yvs_com_creneau_depot; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_creneau_depot (
                                              id bigint NOT NULL,
                                              tranche bigint,
                                              depot integer,
                                              actif boolean DEFAULT true,
                                              permanent boolean,
                                              date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                              date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_com_creneau_depot OWNER TO postgres;

--
-- Name: yvs_com_creneau_depot_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_creneau_depot_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_creneau_depot_id_seq OWNER TO postgres;

--
-- Name: yvs_com_creneau_depot_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_creneau_depot_id_seq OWNED BY public.yvs_com_creneau_depot.id;


--
-- Name: yvs_com_creneau_horaire_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_creneau_horaire_users (
                                                      id bigint NOT NULL,
                                                      users bigint,
                                                      creneau_depot bigint,
                                                      actif boolean DEFAULT true,
                                                      date_travail date,
                                                      author bigint,
                                                      permanent boolean DEFAULT false,
                                                      type character varying DEFAULT 'V'::character varying,
                                                      creneau_point bigint,
                                                      date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                      date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_com_creneau_horaire_users OWNER TO postgres;

--
-- Name: yvs_com_creneau_horaire_users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_creneau_horaire_users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_creneau_horaire_users_id_seq OWNER TO postgres;

--
-- Name: yvs_com_creneau_horaire_users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_creneau_horaire_users_id_seq OWNED BY public.yvs_com_creneau_horaire_users.id;


--
-- Name: yvs_com_creneau_point; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_creneau_point (
                                              id bigint NOT NULL,
                                              tranche bigint,
                                              point integer,
                                              actif boolean DEFAULT true,
                                              permanent boolean,
                                              date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                              date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_com_creneau_point OWNER TO postgres;

--
-- Name: yvs_com_creneau_point_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_creneau_point_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_creneau_point_id_seq OWNER TO postgres;

--
-- Name: yvs_com_creneau_point_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_creneau_point_id_seq OWNED BY public.yvs_com_creneau_point.id;


--
-- Name: yvs_com_doc_ventes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_doc_ventes (
                                           id bigint NOT NULL,
                                           num_piece character varying,
                                           type_doc character varying,
                                           statut character varying,
                                           client bigint,
                                           categorie_comptable bigint,
                                           nom_client character varying,
                                           document_lie bigint,
                                           livreur bigint,
                                           num_doc character varying,
                                           entete_doc bigint,
                                           heure_doc time without time zone,
                                           montant_avance double precision DEFAULT 0,
                                           date_save timestamp without time zone DEFAULT now(),
                                           mouv_stock boolean DEFAULT false,
                                           impression integer DEFAULT 0,
                                           author bigint,
                                           date_solder date DEFAULT ('now'::text)::date,
                                           date_livraison date DEFAULT ('now'::text)::date,
                                           cloturer boolean,
                                           date_cloturer date DEFAULT ('now'::text)::date,
                                           valider_by bigint,
                                           date_valider date DEFAULT ('now'::text)::date,
                                           annuler_by bigint,
                                           date_annuler date DEFAULT ('now'::text)::date,
                                           depot_livrer bigint,
                                           tranche_livrer bigint,
                                           description character varying(255),
                                           model_reglement integer,
                                           statut_livre character varying DEFAULT 'W'::character varying,
                                           statut_regle character varying DEFAULT 'W'::character varying,
                                           date_livraison_prevu timestamp without time zone,
                                           cloturer_by bigint,
                                           adresse bigint,
                                           livraison_auto boolean DEFAULT false,
                                           date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                           commision double precision DEFAULT 0,
                                           tiers bigint,
                                           etape_total integer,
                                           etape_valide integer,
                                           numero_externe character varying,
                                           operateur bigint,
                                           telephone character varying,
                                           comptabilise boolean DEFAULT false,
                                           nature character varying DEFAULT 'VENTE'::character varying,
                                           execute_trigger character varying,
                                           notes text
);


ALTER TABLE public.yvs_com_doc_ventes OWNER TO postgres;

--
-- Name: yvs_com_entete_doc_vente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_entete_doc_vente (
                                                 id bigint NOT NULL,
                                                 creneau bigint,
                                                 date_entete date,
                                                 etat character varying,
                                                 author bigint,
                                                 cloturer boolean DEFAULT false,
                                                 date_cloturer date,
                                                 statut_livre character varying DEFAULT 'W'::character varying,
                                                 statut_regle character varying DEFAULT 'W'::character varying,
                                                 cloturer_by bigint,
                                                 date_valider date,
                                                 valider_by bigint,
                                                 agence bigint,
                                                 date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                 date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_com_entete_doc_vente OWNER TO postgres;

--
-- Name: yvs_com_entete_doc_vente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_entete_doc_vente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_entete_doc_vente_id_seq OWNER TO postgres;

--
-- Name: yvs_com_entete_doc_vente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_entete_doc_vente_id_seq OWNED BY public.yvs_com_entete_doc_vente.id;


--
-- Name: yvs_com_grille_remise; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_grille_remise (
                                              id bigint NOT NULL,
                                              montant_minimal double precision DEFAULT 0,
                                              montant_maximal double precision DEFAULT 0,
                                              montant_remise double precision DEFAULT 0,
                                              nature_montant character varying,
                                              remise bigint,
                                              author bigint,
                                              base character varying DEFAULT 'QTE'::character varying,
                                              date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                              date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                              execute_trigger character varying
);


ALTER TABLE public.yvs_com_grille_remise OWNER TO postgres;

--
-- Name: yvs_com_grille_remise_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_grille_remise_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_grille_remise_id_seq OWNER TO postgres;

--
-- Name: yvs_com_grille_remise_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_grille_remise_id_seq OWNED BY public.yvs_com_grille_remise.id;


--
-- Name: yvs_com_grille_ristourne; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_grille_ristourne (
                                                 id bigint NOT NULL,
                                                 montant_minimal double precision DEFAULT 0,
                                                 montant_maximal double precision DEFAULT 0,
                                                 montant_ristourne double precision DEFAULT 0,
                                                 nature_montant character varying,
                                                 ristourne bigint,
                                                 author bigint,
                                                 base character varying DEFAULT 'QTE'::character varying,
                                                 date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                 date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                 article bigint,
                                                 conditionnement bigint,
                                                 execute_trigger character varying
);


ALTER TABLE public.yvs_com_grille_ristourne OWNER TO postgres;

--
-- Name: yvs_com_grille_ristourne_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_grille_ristourne_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_grille_ristourne_id_seq OWNER TO postgres;

--
-- Name: yvs_com_grille_ristourne_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_grille_ristourne_id_seq OWNED BY public.yvs_com_grille_ristourne.id;


--
-- Name: yvs_com_parametre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_parametre (
                                          id bigint NOT NULL,
                                          reglement_auto boolean,
                                          societe bigint,
                                          document_mouv_achat character varying DEFAULT 'BL'::character varying,
                                          document_mouv_vente character varying DEFAULT 'F'::character varying,
                                          mode_inventaire character varying DEFAULT 'P'::character varying,
                                          author bigint,
                                          seuil_fsseur double precision DEFAULT 0,
                                          seuil_client double precision DEFAULT 0,
                                          duree_inactiv integer DEFAULT 0,
                                          date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                          date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                          converter integer DEFAULT 0,
                                          jour_usine integer,
                                          converter_cs integer DEFAULT 2,
                                          jour_debut_mois integer DEFAULT 21,
                                          facture_outside_seuil boolean,
                                          document_generer_from_ecart character varying DEFAULT 'RE'::character varying
);


ALTER TABLE public.yvs_com_parametre OWNER TO postgres;

--
-- Name: COLUMN yvs_com_parametre.document_mouv_achat; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_com_parametre.document_mouv_achat IS 'BL pour Bon Livraison
F pour Facture';


--
-- Name: COLUMN yvs_com_parametre.document_mouv_vente; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_com_parametre.document_mouv_vente IS 'BL pour Bon Commande
F pour Facture';


--
-- Name: COLUMN yvs_com_parametre.mode_inventaire; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_com_parametre.mode_inventaire IS 'P pour Permanent
I pour Intermitant';


--
-- Name: COLUMN yvs_com_parametre.facture_outside_seuil; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_com_parametre.facture_outside_seuil IS 'Decide si l''on peut facturer à un client ayant depasser son seuil';


--
-- Name: COLUMN yvs_com_parametre.document_generer_from_ecart; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_com_parametre.document_generer_from_ecart IS 'RE pour retenue, CR pour crédit';


--
-- Name: yvs_com_parametre_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_parametre_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_parametre_id_seq OWNER TO postgres;

--
-- Name: yvs_com_parametre_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_parametre_id_seq OWNED BY public.yvs_com_parametre.id;


--
-- Name: yvs_com_parametre_vente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_parametre_vente (
                                                id integer NOT NULL,
                                                jour_anterieur integer DEFAULT 0,
                                                author bigint,
                                                agence bigint,
                                                comptabilisation_auto boolean DEFAULT false,
                                                comptabilisation_mode character varying DEFAULT 'D'::character varying,
                                                date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                paie_without_valide boolean DEFAULT true,
                                                nb_fiche_max integer DEFAULT 0,
                                                generer_facture_auto boolean DEFAULT false,
                                                model_facture_vente character varying DEFAULT 'facture_vente'::character varying,
                                                sell_lower_pr boolean DEFAULT true
);


ALTER TABLE public.yvs_com_parametre_vente OWNER TO postgres;

--
-- Name: COLUMN yvs_com_parametre_vente.comptabilisation_mode; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_com_parametre_vente.comptabilisation_mode IS 'D pour <par document> H pour <par journal> P pour <par periode>';


--
-- Name: yvs_com_parametre_vente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_parametre_vente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_parametre_vente_id_seq OWNER TO postgres;

--
-- Name: yvs_com_parametre_vente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_parametre_vente_id_seq OWNED BY public.yvs_com_parametre_vente.id;


--
-- Name: yvs_com_plan_ristourne; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_plan_ristourne (
                                               id bigint NOT NULL,
                                               actif boolean,
                                               reference character varying,
                                               societe bigint,
                                               date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                               date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_com_plan_ristourne OWNER TO postgres;

--
-- Name: yvs_com_plan_ristourne_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_plan_ristourne_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_plan_ristourne_id_seq OWNER TO postgres;

--
-- Name: yvs_com_plan_ristourne_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_plan_ristourne_id_seq OWNED BY public.yvs_com_plan_ristourne.id;


--
-- Name: yvs_com_rabais; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_rabais (
                                       id bigint NOT NULL,
                                       montant double precision DEFAULT 0,
                                       date_debut date,
                                       date_fin date,
                                       permanent boolean DEFAULT false,
                                       actif boolean DEFAULT false,
                                       article bigint,
                                       date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                       date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_com_rabais OWNER TO postgres;

--
-- Name: yvs_com_rabais_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_rabais_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_rabais_id_seq OWNER TO postgres;

--
-- Name: yvs_com_rabais_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_rabais_id_seq OWNED BY public.yvs_com_rabais.id;


--
-- Name: yvs_com_remise; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_remise (
                                       id bigint NOT NULL,
                                       ref_remise character varying,
                                       permanent boolean DEFAULT false,
                                       actif boolean DEFAULT false,
                                       date_debut date,
                                       date_fin date,
                                       societe bigint,
                                       author bigint,
                                       date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                       date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                       description character varying
);


ALTER TABLE public.yvs_com_remise OWNER TO postgres;

--
-- Name: yvs_com_remise_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_remise_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_remise_id_seq OWNER TO postgres;

--
-- Name: yvs_com_remise_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_remise_id_seq OWNED BY public.yvs_com_remise.id;


--
-- Name: yvs_com_ristourne; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_ristourne (
                                          id bigint NOT NULL,
                                          date_debut date,
                                          date_fin date,
                                          permanent boolean DEFAULT false,
                                          actif boolean DEFAULT false,
                                          author bigint,
                                          article bigint,
                                          plan bigint,
                                          nature character(1) DEFAULT 'R'::bpchar,
                                          date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                          date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                          conditionnement bigint,
                                          famille bigint
);


ALTER TABLE public.yvs_com_ristourne OWNER TO postgres;

--
-- Name: yvs_com_ristourne_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_ristourne_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_ristourne_id_seq OWNER TO postgres;

--
-- Name: yvs_com_ristourne_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_ristourne_id_seq OWNED BY public.yvs_com_ristourne.id;


--
-- Name: yvs_com_taxe_contenu_vente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_com_taxe_contenu_vente (
                                                   id bigint NOT NULL,
                                                   contenu bigint,
                                                   taxe bigint,
                                                   montant double precision,
                                                   author bigint,
                                                   date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                   date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_com_taxe_contenu_vente OWNER TO postgres;

--
-- Name: yvs_com_taxe_contenu_vente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_com_taxe_contenu_vente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_com_taxe_contenu_vente_id_seq OWNER TO postgres;

--
-- Name: yvs_com_taxe_contenu_vente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_com_taxe_contenu_vente_id_seq OWNED BY public.yvs_com_taxe_contenu_vente.id;


--
-- Name: yvs_compta_acompte_client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_compta_acompte_client (
                                                  id bigint NOT NULL,
                                                  montant double precision,
                                                  date_acompte date,
                                                  num_refrence character varying,
                                                  commentaire character varying,
                                                  author bigint,
                                                  client bigint,
                                                  caisse bigint,
                                                  statut character(1) DEFAULT 'W'::bpchar,
                                                  date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                  date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                  model bigint,
                                                  reference_externe character varying,
                                                  date_paiement date,
                                                  statut_notif character(1) DEFAULT 'W'::bpchar,
                                                  nature character(1) DEFAULT 'A'::bpchar,
                                                  repartir_automatique boolean DEFAULT true,
                                                  comptabilise boolean DEFAULT false
);


ALTER TABLE public.yvs_compta_acompte_client OWNER TO postgres;

--
-- Name: yvs_compta_acompte_client_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_compta_acompte_client_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_compta_acompte_client_id_seq OWNER TO postgres;

--
-- Name: yvs_compta_acompte_client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_compta_acompte_client_id_seq OWNED BY public.yvs_compta_acompte_client.id;


--
-- Name: yvs_compta_caisse_piece_vente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_compta_caisse_piece_vente (
                                                      id bigint NOT NULL,
                                                      numero_piece character varying,
                                                      montant double precision,
                                                      statut_piece character(1),
                                                      vente bigint,
                                                      caisse bigint,
                                                      author bigint,
                                                      date_piece date,
                                                      date_paiement date,
                                                      note character varying,
                                                      model bigint,
                                                      caissier bigint,
                                                      date_paiment_prevu date,
                                                      reference_externe character varying,
                                                      date_valide date,
                                                      valide_by bigint,
                                                      date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                      date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                      montant_recu double precision DEFAULT 0,
                                                      mouvement character(1) DEFAULT 'R'::bpchar,
                                                      parent bigint,
                                                      comptabilise boolean DEFAULT false,
                                                      verouille boolean DEFAULT false,
                                                      execute_trigger character varying
);


ALTER TABLE public.yvs_compta_caisse_piece_vente OWNER TO postgres;

--
-- Name: yvs_compta_caisse_piece_vente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_compta_caisse_piece_vente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_compta_caisse_piece_vente_id_seq OWNER TO postgres;

--
-- Name: yvs_compta_caisse_piece_vente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_compta_caisse_piece_vente_id_seq OWNED BY public.yvs_compta_caisse_piece_vente.id;


--
-- Name: yvs_compta_caisse_piece_virement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_compta_caisse_piece_virement (
                                                         id bigint NOT NULL,
                                                         numero_piece character varying,
                                                         montant double precision,
                                                         statut_piece character(1),
                                                         source bigint,
                                                         cible bigint,
                                                         author bigint,
                                                         date_piece date,
                                                         date_paiement timestamp without time zone,
                                                         note character varying,
                                                         model bigint,
                                                         caissier_source bigint,
                                                         caissier_cible bigint,
                                                         date_paiment_prevu date,
                                                         date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                         date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_compta_caisse_piece_virement OWNER TO postgres;

--
-- Name: yvs_compta_caisse_piece_virement_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_compta_caisse_piece_virement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_compta_caisse_piece_virement_id_seq OWNER TO postgres;

--
-- Name: yvs_compta_caisse_piece_virement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_compta_caisse_piece_virement_id_seq OWNED BY public.yvs_compta_caisse_piece_virement.id;


--
-- Name: yvs_compta_notif_reglement_vente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_compta_notif_reglement_vente (
                                                         id bigint NOT NULL,
                                                         piece_vente bigint,
                                                         acompte bigint,
                                                         author bigint,
                                                         date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                                         date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_compta_notif_reglement_vente OWNER TO postgres;

--
-- Name: yvs_compta_notif_reglement_vente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_compta_notif_reglement_vente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_compta_notif_reglement_vente_id_seq OWNER TO postgres;

--
-- Name: yvs_compta_notif_reglement_vente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_compta_notif_reglement_vente_id_seq OWNED BY public.yvs_compta_notif_reglement_vente.id;


--
-- Name: yvs_compta_parametre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_compta_parametre (
                                             id integer NOT NULL,
                                             taille_compte integer DEFAULT 8,
                                             societe bigint,
                                             author bigint,
                                             decimal_arrondi boolean DEFAULT true,
                                             mode_arrondi character varying DEFAULT 'A'::character varying,
                                             multiple_arrondi double precision DEFAULT 5,
                                             valeur_arrondi integer DEFAULT 2,
                                             date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                             date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                             maj_compta_auto_divers boolean DEFAULT false,
                                             maj_compta_statut_divers character(1) DEFAULT 'P'::bpchar,
                                             converter integer DEFAULT 0,
                                             montant_seuil_depense_od double precision DEFAULT 0,
                                             valeur_limite_arrondi double precision DEFAULT 10,
                                             montant_seuil_recette_od double precision DEFAULT 0,
                                             ecart_day_solde_client integer DEFAULT 7,
                                             nombre_ligne_solde_client integer DEFAULT 4,
                                             jour_anterieur_cancel integer DEFAULT 1,
                                             jour_anterieur integer DEFAULT 7,
                                             execute_trigger character varying
);


ALTER TABLE public.yvs_compta_parametre OWNER TO postgres;

--
-- Name: yvs_compta_parametre_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_compta_parametre_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_compta_parametre_id_seq OWNER TO postgres;

--
-- Name: yvs_compta_parametre_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_compta_parametre_id_seq OWNED BY public.yvs_compta_parametre.id;


--
-- Name: yvs_contenu_doc_vente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_contenu_doc_vente_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_contenu_doc_vente_id_seq OWNER TO postgres;

--
-- Name: yvs_contenu_doc_vente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_contenu_doc_vente_id_seq OWNED BY public.yvs_com_contenu_doc_vente.id;


--
-- Name: yvs_dictionnaire; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_dictionnaire (
                                         id bigint NOT NULL,
                                         libele character varying(255),
                                         titre character varying(255),
                                         actif boolean DEFAULT true,
                                         parent integer,
                                         abreviation character varying,
                                         date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                         date_save timestamp without time zone DEFAULT ('now'::text)::date
);


ALTER TABLE public.yvs_dictionnaire OWNER TO postgres;

--
-- Name: yvs_dictionnaire_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_dictionnaire_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_dictionnaire_id_seq OWNER TO postgres;

--
-- Name: yvs_dictionnaire_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_dictionnaire_id_seq OWNED BY public.yvs_dictionnaire.id;


--
-- Name: yvs_doc_ventes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_doc_ventes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_doc_ventes_id_seq OWNER TO postgres;

--
-- Name: yvs_doc_ventes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_doc_ventes_id_seq OWNED BY public.yvs_com_doc_ventes.id;


--
-- Name: yvs_grh_tranche_horaire; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_grh_tranche_horaire (
                                                id bigint NOT NULL,
                                                titre character varying,
                                                heure_debut time without time zone,
                                                heure_fin time without time zone,
                                                type_journee character varying(5),
                                                actif boolean,
                                                date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                                societe bigint
);


ALTER TABLE public.yvs_grh_tranche_horaire OWNER TO postgres;

--
-- Name: yvs_grh_tranche_horaire_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_grh_tranche_horaire_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_grh_tranche_horaire_id_seq OWNER TO postgres;

--
-- Name: yvs_grh_tranche_horaire_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_grh_tranche_horaire_id_seq OWNED BY public.yvs_grh_tranche_horaire.id;


--
-- Name: yvs_societes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_societes (
                                     id bigint NOT NULL,
                                     adress_siege character varying(255),
                                     code_abreviation character varying(255),
                                     code_postal character varying(255),
                                     devise character varying(255),
                                     email character varying(255),
                                     forme_juridique character varying(255),
                                     gestva boolean,
                                     last_author character varying(255),
                                     logo character varying(255),
                                     name character varying(255),
                                     numero_registre_comerce character varying(255),
                                     siege character varying(255),
                                     site_web character varying(255),
                                     tel character varying(255),
                                     actif boolean DEFAULT true,
                                     regime_cnps character varying,
                                     date_save timestamp without time zone,
                                     date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                     description character varying(255),
                                     ecart_document integer DEFAULT 30,
                                     a_propos character varying,
                                     numero_contribuable character varying
);


ALTER TABLE public.yvs_societes OWNER TO postgres;

--
-- Name: yvs_societes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_societes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_societes_id_seq OWNER TO postgres;

--
-- Name: yvs_societes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_societes_id_seq OWNED BY public.yvs_societes.id;


--
-- Name: yvs_synchro_data_synchro; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_synchro_data_synchro (
                                                 id bigint NOT NULL,
                                                 id_listen bigint,
                                                 date_save timestamp without time zone,
                                                 id_distant bigint,
                                                 serveur integer
);


ALTER TABLE public.yvs_synchro_data_synchro OWNER TO postgres;

--
-- Name: yvs_synchro_data_synchro_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_synchro_data_synchro_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_synchro_data_synchro_id_seq OWNER TO postgres;

--
-- Name: yvs_synchro_data_synchro_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_synchro_data_synchro_id_seq OWNED BY public.yvs_synchro_data_synchro.id;


--
-- Name: yvs_synchro_listen_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_synchro_listen_table (
                                                 id bigint NOT NULL,
                                                 name_table character varying,
                                                 id_source bigint,
                                                 date_save timestamp without time zone,
                                                 message character varying,
                                                 to_listen boolean,
                                                 action_name character varying,
                                                 ordre bigint NOT NULL,
                                                 author bigint,
                                                 serveur bigint,
                                                 nb_failed integer
);


ALTER TABLE public.yvs_synchro_listen_table OWNER TO postgres;

--
-- Name: COLUMN yvs_synchro_listen_table.action_name; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.yvs_synchro_listen_table.action_name IS 'Précise le type d''action à réaliser sur la table';


--
-- Name: yvs_synchro_listen_table_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_synchro_listen_table_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_synchro_listen_table_id_seq OWNER TO postgres;

--
-- Name: yvs_synchro_listen_table_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_synchro_listen_table_id_seq OWNED BY public.yvs_synchro_listen_table.id;


--
-- Name: yvs_synchro_listen_table_ordre_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_synchro_listen_table_ordre_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_synchro_listen_table_ordre_seq OWNER TO postgres;

--
-- Name: yvs_synchro_listen_table_ordre_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_synchro_listen_table_ordre_seq OWNED BY public.yvs_synchro_listen_table.ordre;


--
-- Name: yvs_synchro_serveurs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_synchro_serveurs (
                                             id bigint NOT NULL,
                                             nom_serveur character varying,
                                             adresse_ip character varying,
                                             actif boolean DEFAULT true,
                                             online boolean DEFAULT false,
                                             database character varying DEFAULT 'lymytz_demo_0'::character varying,
                                             port integer DEFAULT 5432,
                                             users character varying DEFAULT 'postgres'::character varying,
                                             password character varying DEFAULT 'yves1910/'::character varying,
                                             execute_trigger character varying
);


ALTER TABLE public.yvs_synchro_serveurs OWNER TO postgres;

--
-- Name: yvs_synchro_serveurs_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_synchro_serveurs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_synchro_serveurs_id_seq OWNER TO postgres;

--
-- Name: yvs_synchro_serveurs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_synchro_serveurs_id_seq OWNED BY public.yvs_synchro_serveurs.id;


--
-- Name: yvs_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_users (
                                  id bigint NOT NULL,
                                  code_users character varying(255),
                                  password_user character varying(255),
                                  alea_mdp character varying,
                                  nom_users character varying,
                                  photo character varying,
                                  civilite character varying,
                                  agence integer,
                                  date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                  date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                  abbreviation character varying,
                                  actif boolean DEFAULT true
);


ALTER TABLE public.yvs_users OWNER TO postgres;

--
-- Name: yvs_users_agence; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.yvs_users_agence (
                                         id bigint NOT NULL,
                                         date_update timestamp without time zone DEFAULT ('now'::text)::date,
                                         date_save timestamp without time zone DEFAULT ('now'::text)::date,
                                         users bigint NOT NULL,
                                         agence bigint NOT NULL
);


ALTER TABLE public.yvs_users_agence OWNER TO postgres;

--
-- Name: yvs_users_agence_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_users_agence_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_users_agence_id_seq OWNER TO postgres;

--
-- Name: yvs_users_agence_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_users_agence_id_seq OWNED BY public.yvs_users_agence.id;


--
-- Name: yvs_users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.yvs_users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.yvs_users_id_seq OWNER TO postgres;

--
-- Name: yvs_users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.yvs_users_id_seq OWNED BY public.yvs_users.id;


--
-- Name: yvs_agences id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_agences ALTER COLUMN id SET DEFAULT nextval('public.yvs_agences_id_seq'::regclass);


--
-- Name: yvs_base_article_categorie_comptable id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_categorie_comptable ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_article_categorie_comptable_id_seq'::regclass);


--
-- Name: yvs_base_article_categorie_comptable_taxe id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_categorie_comptable_taxe ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_article_categorie_comptable_taxe_id_seq'::regclass);


--
-- Name: yvs_base_article_code_barre id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_code_barre ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_article_code_barre_id_seq'::regclass);


--
-- Name: yvs_base_article_depot id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_depot ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_article_depot_id_seq'::regclass);


--
-- Name: yvs_base_article_point id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_point ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_article_point_id_seq'::regclass);


--
-- Name: yvs_base_articles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_articles ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_articles_id_seq'::regclass);


--
-- Name: yvs_base_caisse id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_caisse_id_seq'::regclass);


--
-- Name: yvs_base_caisse_user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse_user ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_caisse_user_id_seq'::regclass);


--
-- Name: yvs_base_categorie_client id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_categorie_client ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_categorie_client_id_seq'::regclass);


--
-- Name: yvs_base_categorie_comptable id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_categorie_comptable ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_categorie_comptable_id_seq'::regclass);


--
-- Name: yvs_base_classes_stat id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_classes_stat ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_classes_stat_id_seq'::regclass);


--
-- Name: yvs_base_code_acces id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_code_acces ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_code_acces_id_seq'::regclass);


--
-- Name: yvs_base_conditionnement id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_conditionnement ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_conditionnement_id_seq'::regclass);


--
-- Name: yvs_base_conditionnement_point id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_conditionnement_point ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_conditionnement_point_id_seq'::regclass);


--
-- Name: yvs_base_depots id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_depots ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_depots_id_seq'::regclass);


--
-- Name: yvs_base_element_reference id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_element_reference ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_element_reference_id_seq'::regclass);


--
-- Name: yvs_base_exercice id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_exercice ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_exercice_id_seq'::regclass);


--
-- Name: yvs_base_famille_article id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_famille_article ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_famille_article_id_seq'::regclass);


--
-- Name: yvs_base_groupes_article id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_groupes_article ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_groupes_article_id_seq'::regclass);


--
-- Name: yvs_base_liaison_caisse id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_liaison_caisse ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_liaison_caisse_id_seq'::regclass);


--
-- Name: yvs_base_mode_reglement id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_mode_reglement ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_mode_reglement_id_seq'::regclass);


--
-- Name: yvs_base_model_reglement id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_model_reglement ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_model_reglement_id_seq'::regclass);


--
-- Name: yvs_base_modele_reference id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_modele_reference ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_modele_reference_id_seq'::regclass);


--
-- Name: yvs_base_plan_tarifaire id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_plan_tarifaire_id_seq'::regclass);


--
-- Name: yvs_base_plan_tarifaire_tranche id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire_tranche ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_plan_tarifaire_tranche_id_seq'::regclass);


--
-- Name: yvs_base_point_livraison id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_livraison ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_point_livraison_id_seq'::regclass);


--
-- Name: yvs_base_point_vente id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_vente ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_point_vente_id_seq'::regclass);


--
-- Name: yvs_base_point_vente_depot id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_vente_depot ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_point_vente_depot_id_seq'::regclass);


--
-- Name: yvs_base_taxes id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_taxes ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_taxes_id_seq'::regclass);


--
-- Name: yvs_base_tiers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_tiers ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_tiers_id_seq'::regclass);


--
-- Name: yvs_base_unite_mesure id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_unite_mesure ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_unite_mesure_id_seq'::regclass);


--
-- Name: yvs_base_users_acces id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_users_acces ALTER COLUMN id SET DEFAULT nextval('public.yvs_base_users_acces_id_seq'::regclass);


--
-- Name: yvs_com_categorie_tarifaire id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_categorie_tarifaire ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_categorie_tarifaire_id_seq'::regclass);


--
-- Name: yvs_com_client id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_client ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_client_id_seq'::regclass);


--
-- Name: yvs_com_comerciale id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_comerciale ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_comerciale_id_seq'::regclass);


--
-- Name: yvs_com_commercial_point id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_commercial_point ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_commercial_point_id_seq'::regclass);


--
-- Name: yvs_com_commercial_vente id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_commercial_vente ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_commercial_vente_id_seq'::regclass);


--
-- Name: yvs_com_contenu_doc_vente id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_contenu_doc_vente ALTER COLUMN id SET DEFAULT nextval('public.yvs_contenu_doc_vente_id_seq'::regclass);


--
-- Name: yvs_com_creneau_depot id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_depot ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_creneau_depot_id_seq'::regclass);


--
-- Name: yvs_com_creneau_horaire_users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_horaire_users ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_creneau_horaire_users_id_seq'::regclass);


--
-- Name: yvs_com_creneau_point id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_point ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_creneau_point_id_seq'::regclass);


--
-- Name: yvs_com_doc_ventes id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes ALTER COLUMN id SET DEFAULT nextval('public.yvs_doc_ventes_id_seq'::regclass);


--
-- Name: yvs_com_entete_doc_vente id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_entete_doc_vente ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_entete_doc_vente_id_seq'::regclass);


--
-- Name: yvs_com_grille_remise id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_grille_remise ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_grille_remise_id_seq'::regclass);


--
-- Name: yvs_com_grille_ristourne id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_grille_ristourne ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_grille_ristourne_id_seq'::regclass);


--
-- Name: yvs_com_parametre id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_parametre ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_parametre_id_seq'::regclass);


--
-- Name: yvs_com_parametre_vente id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_parametre_vente ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_parametre_vente_id_seq'::regclass);


--
-- Name: yvs_com_plan_ristourne id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_plan_ristourne ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_plan_ristourne_id_seq'::regclass);


--
-- Name: yvs_com_rabais id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_rabais ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_rabais_id_seq'::regclass);


--
-- Name: yvs_com_remise id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_remise ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_remise_id_seq'::regclass);


--
-- Name: yvs_com_ristourne id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_ristourne ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_ristourne_id_seq'::regclass);


--
-- Name: yvs_com_taxe_contenu_vente id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_taxe_contenu_vente ALTER COLUMN id SET DEFAULT nextval('public.yvs_com_taxe_contenu_vente_id_seq'::regclass);


--
-- Name: yvs_compta_acompte_client id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_acompte_client ALTER COLUMN id SET DEFAULT nextval('public.yvs_compta_acompte_client_id_seq'::regclass);


--
-- Name: yvs_compta_caisse_piece_vente id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_vente ALTER COLUMN id SET DEFAULT nextval('public.yvs_compta_caisse_piece_vente_id_seq'::regclass);


--
-- Name: yvs_compta_caisse_piece_virement id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_virement ALTER COLUMN id SET DEFAULT nextval('public.yvs_compta_caisse_piece_virement_id_seq'::regclass);


--
-- Name: yvs_compta_notif_reglement_vente id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_notif_reglement_vente ALTER COLUMN id SET DEFAULT nextval('public.yvs_compta_notif_reglement_vente_id_seq'::regclass);


--
-- Name: yvs_compta_parametre id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_parametre ALTER COLUMN id SET DEFAULT nextval('public.yvs_compta_parametre_id_seq'::regclass);


--
-- Name: yvs_dictionnaire id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_dictionnaire ALTER COLUMN id SET DEFAULT nextval('public.yvs_dictionnaire_id_seq'::regclass);


--
-- Name: yvs_grh_tranche_horaire id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_grh_tranche_horaire ALTER COLUMN id SET DEFAULT nextval('public.yvs_grh_tranche_horaire_id_seq'::regclass);


--
-- Name: yvs_societes id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_societes ALTER COLUMN id SET DEFAULT nextval('public.yvs_societes_id_seq'::regclass);


--
-- Name: yvs_synchro_data_synchro id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_synchro_data_synchro ALTER COLUMN id SET DEFAULT nextval('public.yvs_synchro_data_synchro_id_seq'::regclass);


--
-- Name: yvs_synchro_listen_table id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_synchro_listen_table ALTER COLUMN id SET DEFAULT nextval('public.yvs_synchro_listen_table_id_seq'::regclass);


--
-- Name: yvs_synchro_listen_table ordre; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_synchro_listen_table ALTER COLUMN ordre SET DEFAULT nextval('public.yvs_synchro_listen_table_ordre_seq'::regclass);


--
-- Name: yvs_synchro_serveurs id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_synchro_serveurs ALTER COLUMN id SET DEFAULT nextval('public.yvs_synchro_serveurs_id_seq'::regclass);


--
-- Name: yvs_users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_users ALTER COLUMN id SET DEFAULT nextval('public.yvs_users_id_seq'::regclass);


--
-- Name: yvs_users_agence id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_users_agence ALTER COLUMN id SET DEFAULT nextval('public.yvs_users_agence_id_seq'::regclass);


--
-- Name: yvs_base_mode_reglement mdr_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_mode_reglement
    ADD CONSTRAINT mdr_pk PRIMARY KEY (id);


--
-- Name: yvs_agences yvs_agences_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_agences
    ADD CONSTRAINT yvs_agences_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_articles yvs_articles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_articles
    ADD CONSTRAINT yvs_articles_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_article_code_barre yvs_base_article_code_barre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_code_barre
    ADD CONSTRAINT yvs_base_article_code_barre_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_article_depot yvs_base_article_depot_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_depot
    ADD CONSTRAINT yvs_base_article_depot_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_article_point yvs_base_articles_point_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_point
    ADD CONSTRAINT yvs_base_articles_point_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_caisse yvs_base_caisse_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse
    ADD CONSTRAINT yvs_base_caisse_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_caisse_user yvs_base_caisse_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse_user
    ADD CONSTRAINT yvs_base_caisse_user_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_categorie_comptable yvs_base_categorie_comptable_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_categorie_comptable
    ADD CONSTRAINT yvs_base_categorie_comptable_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_classes_stat yvs_base_classes_stat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_classes_stat
    ADD CONSTRAINT yvs_base_classes_stat_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_code_acces yvs_base_code_acces_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_code_acces
    ADD CONSTRAINT yvs_base_code_acces_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_article_categorie_comptable yvs_base_compte_article_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_categorie_comptable
    ADD CONSTRAINT yvs_base_compte_article_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_conditionnement_point yvs_base_conditionnement_point_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_conditionnement_point
    ADD CONSTRAINT yvs_base_conditionnement_point_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_element_reference yvs_base_element_reference_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_element_reference
    ADD CONSTRAINT yvs_base_element_reference_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_liaison_caisse yvs_base_liaison_caisse_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_liaison_caisse
    ADD CONSTRAINT yvs_base_liaison_caisse_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_model_reglement yvs_base_model_reglement_fk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_model_reglement
    ADD CONSTRAINT yvs_base_model_reglement_fk PRIMARY KEY (id);


--
-- Name: yvs_base_modele_reference yvs_base_modele_reference_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_modele_reference
    ADD CONSTRAINT yvs_base_modele_reference_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_plan_tarifaire_tranche yvs_base_plan_tarifaire_tranche_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire_tranche
    ADD CONSTRAINT yvs_base_plan_tarifaire_tranche_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_point_livraison yvs_base_point_livraison_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_livraison
    ADD CONSTRAINT yvs_base_point_livraison_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_point_vente_depot yvs_base_point_vente_depot_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_vente_depot
    ADD CONSTRAINT yvs_base_point_vente_depot_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_point_vente yvs_base_point_vente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_vente
    ADD CONSTRAINT yvs_base_point_vente_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_article_categorie_comptable_taxe yvs_base_taxe_article_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_categorie_comptable_taxe
    ADD CONSTRAINT yvs_base_taxe_article_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_users_acces yvs_base_users_acces_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_users_acces
    ADD CONSTRAINT yvs_base_users_acces_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_categorie_client yvs_com_categorie_client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_categorie_client
    ADD CONSTRAINT yvs_com_categorie_client_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_plan_tarifaire yvs_com_categorie_tarifaire_client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire
    ADD CONSTRAINT yvs_com_categorie_tarifaire_client_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_categorie_tarifaire yvs_com_categorie_tarifaire_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_categorie_tarifaire
    ADD CONSTRAINT yvs_com_categorie_tarifaire_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_client yvs_com_client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_client
    ADD CONSTRAINT yvs_com_client_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_comerciale yvs_com_comerciale_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_comerciale
    ADD CONSTRAINT yvs_com_comerciale_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_commercial_point yvs_com_commercial_point_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_commercial_point
    ADD CONSTRAINT yvs_com_commercial_point_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_commercial_vente yvs_com_commercial_vente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_commercial_vente
    ADD CONSTRAINT yvs_com_commercial_vente_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_creneau_point yvs_com_creneau_depot_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_point
    ADD CONSTRAINT yvs_com_creneau_depot_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_creneau_horaire_users yvs_com_creneau_horaire_employe_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_horaire_users
    ADD CONSTRAINT yvs_com_creneau_horaire_employe_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_creneau_depot yvs_com_creneau_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_depot
    ADD CONSTRAINT yvs_com_creneau_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_entete_doc_vente yvs_com_entete_doc_vente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_entete_doc_vente
    ADD CONSTRAINT yvs_com_entete_doc_vente_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_grille_remise yvs_com_grille_remise_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_grille_remise
    ADD CONSTRAINT yvs_com_grille_remise_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_grille_ristourne yvs_com_grille_ristourne_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_grille_ristourne
    ADD CONSTRAINT yvs_com_grille_ristourne_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_parametre yvs_com_parametre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_parametre
    ADD CONSTRAINT yvs_com_parametre_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_parametre_vente yvs_com_parametre_vente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_parametre_vente
    ADD CONSTRAINT yvs_com_parametre_vente_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_plan_ristourne yvs_com_plan_ristourne_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_plan_ristourne
    ADD CONSTRAINT yvs_com_plan_ristourne_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_rabais yvs_com_rabais_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_rabais
    ADD CONSTRAINT yvs_com_rabais_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_remise yvs_com_remise_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_remise
    ADD CONSTRAINT yvs_com_remise_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_ristourne yvs_com_ristourne_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_ristourne
    ADD CONSTRAINT yvs_com_ristourne_pkey PRIMARY KEY (id);


--
-- Name: yvs_compta_acompte_client yvs_compta_acompte_client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_acompte_client
    ADD CONSTRAINT yvs_compta_acompte_client_pkey PRIMARY KEY (id);


--
-- Name: yvs_compta_caisse_piece_vente yvs_compta_caisse_piece_vente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_vente
    ADD CONSTRAINT yvs_compta_caisse_piece_vente_pkey PRIMARY KEY (id);


--
-- Name: yvs_compta_caisse_piece_virement yvs_compta_caisse_piece_virement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_virement
    ADD CONSTRAINT yvs_compta_caisse_piece_virement_pkey PRIMARY KEY (id);


--
-- Name: yvs_compta_notif_reglement_vente yvs_compta_notif_reglement_vente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_notif_reglement_vente
    ADD CONSTRAINT yvs_compta_notif_reglement_vente_pkey PRIMARY KEY (id);


--
-- Name: yvs_compta_parametre yvs_compta_parametre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_parametre
    ADD CONSTRAINT yvs_compta_parametre_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_conditionnement yvs_conditionnement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_conditionnement
    ADD CONSTRAINT yvs_conditionnement_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_contenu_doc_vente yvs_contenu_doc_vente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_contenu_doc_vente
    ADD CONSTRAINT yvs_contenu_doc_vente_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_taxe_contenu_vente yvs_cout_sup_contenu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_taxe_contenu_vente
    ADD CONSTRAINT yvs_cout_sup_contenu_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_depots yvs_depots_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_depots
    ADD CONSTRAINT yvs_depots_pkey PRIMARY KEY (id);


--
-- Name: yvs_dictionnaire yvs_dictionnaire_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_dictionnaire
    ADD CONSTRAINT yvs_dictionnaire_pkey PRIMARY KEY (id);


--
-- Name: yvs_com_doc_ventes yvs_doc_ventes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_doc_ventes_pkey PRIMARY KEY (id);


--
-- Name: yvs_grh_tranche_horaire yvs_grh_tranche_horaire_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_grh_tranche_horaire
    ADD CONSTRAINT yvs_grh_tranche_horaire_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_groupes_article yvs_groupesproduits_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_groupes_article
    ADD CONSTRAINT yvs_groupesproduits_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_exercice yvs_mut_exercice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_exercice
    ADD CONSTRAINT yvs_mut_exercice_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_famille_article yvs_prod_famille_article_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_famille_article
    ADD CONSTRAINT yvs_prod_famille_article_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_unite_mesure yvs_prod_unite_masse_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_unite_mesure
    ADD CONSTRAINT yvs_prod_unite_masse_pkey PRIMARY KEY (id);


--
-- Name: yvs_societes yvs_societes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_societes
    ADD CONSTRAINT yvs_societes_pkey PRIMARY KEY (id);


--
-- Name: yvs_synchro_data_synchro yvs_synchro_data_synchro_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_synchro_data_synchro
    ADD CONSTRAINT yvs_synchro_data_synchro_pkey PRIMARY KEY (id);


--
-- Name: yvs_synchro_listen_table yvs_synchro_listen_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_synchro_listen_table
    ADD CONSTRAINT yvs_synchro_listen_table_pkey PRIMARY KEY (id);


--
-- Name: yvs_synchro_serveurs yvs_synchro_serveur_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_synchro_serveurs
    ADD CONSTRAINT yvs_synchro_serveur_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_taxes yvs_taxes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_taxes
    ADD CONSTRAINT yvs_taxes_pkey PRIMARY KEY (id);


--
-- Name: yvs_base_tiers yvs_tiers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_tiers
    ADD CONSTRAINT yvs_tiers_pkey PRIMARY KEY (id);


--
-- Name: yvs_users_agence yvs_users_agence_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_users_agence
    ADD CONSTRAINT yvs_users_agence_pkey PRIMARY KEY (id);


--
-- Name: yvs_users yvs_users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_users
    ADD CONSTRAINT yvs_users_pkey PRIMARY KEY (id);


--
-- Name: code_users_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX code_users_idx ON public.yvs_users USING btree (code_users);


--
-- Name: yvs_base_model_reglement_reference; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX yvs_base_model_reglement_reference ON public.yvs_base_model_reglement USING btree (reference);


--
-- Name: yvs_base_mode_reglement fk_yvs_model_de_reglement_societe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_mode_reglement
    ADD CONSTRAINT fk_yvs_model_de_reglement_societe FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_agences yvs_agences_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_agences
    ADD CONSTRAINT yvs_agences_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_articles yvs_articles_famille_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_articles
    ADD CONSTRAINT yvs_articles_famille_fkey FOREIGN KEY (famille) REFERENCES public.yvs_base_famille_article(id);


--
-- Name: yvs_base_articles yvs_articles_groupe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_articles
    ADD CONSTRAINT yvs_articles_groupe_fkey FOREIGN KEY (groupe) REFERENCES public.yvs_base_groupes_article(id);


--
-- Name: yvs_base_article_categorie_comptable yvs_base_article_categorie_comptable_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_categorie_comptable
    ADD CONSTRAINT yvs_base_article_categorie_comptable_article_fkey FOREIGN KEY (article) REFERENCES public.yvs_base_articles(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_base_article_categorie_comptable yvs_base_article_categorie_comptable_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_categorie_comptable
    ADD CONSTRAINT yvs_base_article_categorie_comptable_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_article_categorie_comptable_taxe yvs_base_article_categorie_comptable_taxe_article_categorie_fke; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_categorie_comptable_taxe
    ADD CONSTRAINT yvs_base_article_categorie_comptable_taxe_article_categorie_fke FOREIGN KEY (article_categorie) REFERENCES public.yvs_base_article_categorie_comptable(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_base_article_categorie_comptable_taxe yvs_base_article_categorie_comptable_taxe_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_categorie_comptable_taxe
    ADD CONSTRAINT yvs_base_article_categorie_comptable_taxe_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_article_code_barre yvs_base_article_code_barre_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_code_barre
    ADD CONSTRAINT yvs_base_article_code_barre_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_article_code_barre yvs_base_article_code_barre_conditionnement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_code_barre
    ADD CONSTRAINT yvs_base_article_code_barre_conditionnement_fkey FOREIGN KEY (conditionnement) REFERENCES public.yvs_base_conditionnement(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_base_article_depot yvs_base_article_depot_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_depot
    ADD CONSTRAINT yvs_base_article_depot_article_fkey FOREIGN KEY (article) REFERENCES public.yvs_base_articles(id);


--
-- Name: yvs_base_article_depot yvs_base_article_depot_depot_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_depot
    ADD CONSTRAINT yvs_base_article_depot_depot_fkey FOREIGN KEY (depot) REFERENCES public.yvs_base_depots(id);


--
-- Name: yvs_base_article_point yvs_base_article_point_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_point
    ADD CONSTRAINT yvs_base_article_point_article_fkey FOREIGN KEY (article) REFERENCES public.yvs_base_articles(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_base_article_point yvs_base_article_point_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_point
    ADD CONSTRAINT yvs_base_article_point_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_article_point yvs_base_article_point_poitn_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_point
    ADD CONSTRAINT yvs_base_article_point_poitn_fkey FOREIGN KEY (point) REFERENCES public.yvs_base_point_vente(id);


--
-- Name: yvs_base_articles yvs_base_articles_classe1_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_articles
    ADD CONSTRAINT yvs_base_articles_classe1_fkey FOREIGN KEY (classe1) REFERENCES public.yvs_base_classes_stat(id);


--
-- Name: yvs_base_articles yvs_base_articles_classe2_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_articles
    ADD CONSTRAINT yvs_base_articles_classe2_fkey FOREIGN KEY (classe2) REFERENCES public.yvs_base_classes_stat(id);


--
-- Name: yvs_base_caisse yvs_base_caisse_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse
    ADD CONSTRAINT yvs_base_caisse_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_caisse yvs_base_caisse_caissier_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse
    ADD CONSTRAINT yvs_base_caisse_caissier_fkey FOREIGN KEY (caissier) REFERENCES public.yvs_users(id);


--
-- Name: yvs_base_caisse yvs_base_caisse_code_acces_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse
    ADD CONSTRAINT yvs_base_caisse_code_acces_fkey FOREIGN KEY (code_acces) REFERENCES public.yvs_base_code_acces(id) ON DELETE SET NULL;


--
-- Name: yvs_base_caisse yvs_base_caisse_mode_reg_defaut_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse
    ADD CONSTRAINT yvs_base_caisse_mode_reg_defaut_fkey FOREIGN KEY (mode_reg_defaut) REFERENCES public.yvs_base_mode_reglement(id);


--
-- Name: yvs_base_caisse yvs_base_caisse_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse
    ADD CONSTRAINT yvs_base_caisse_parent_fkey FOREIGN KEY (parent) REFERENCES public.yvs_base_caisse(id);


--
-- Name: yvs_base_caisse_user yvs_base_caisse_user_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse_user
    ADD CONSTRAINT yvs_base_caisse_user_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_caisse_user yvs_base_caisse_user_id_caisse_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse_user
    ADD CONSTRAINT yvs_base_caisse_user_id_caisse_fkey FOREIGN KEY (id_caisse) REFERENCES public.yvs_base_caisse(id);


--
-- Name: yvs_base_caisse_user yvs_base_caisse_user_id_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_caisse_user
    ADD CONSTRAINT yvs_base_caisse_user_id_user_fkey FOREIGN KEY (id_user) REFERENCES public.yvs_users(id);


--
-- Name: yvs_base_categorie_comptable yvs_base_categorie_comptable_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_categorie_comptable
    ADD CONSTRAINT yvs_base_categorie_comptable_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_categorie_comptable yvs_base_categorie_comptable_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_categorie_comptable
    ADD CONSTRAINT yvs_base_categorie_comptable_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_classes_stat yvs_base_classes_stat_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_classes_stat
    ADD CONSTRAINT yvs_base_classes_stat_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_classes_stat yvs_base_classes_stat_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_classes_stat
    ADD CONSTRAINT yvs_base_classes_stat_parent_fkey FOREIGN KEY (parent) REFERENCES public.yvs_base_classes_stat(id);


--
-- Name: yvs_base_classes_stat yvs_base_classes_stat_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_classes_stat
    ADD CONSTRAINT yvs_base_classes_stat_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_code_acces yvs_base_code_acces_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_code_acces
    ADD CONSTRAINT yvs_base_code_acces_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_code_acces yvs_base_code_acces_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_code_acces
    ADD CONSTRAINT yvs_base_code_acces_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_article_categorie_comptable yvs_base_compte_article_categorie_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_categorie_comptable
    ADD CONSTRAINT yvs_base_compte_article_categorie_fkey FOREIGN KEY (categorie) REFERENCES public.yvs_base_categorie_comptable(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_base_conditionnement yvs_base_conditionnement_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_conditionnement
    ADD CONSTRAINT yvs_base_conditionnement_article_fkey FOREIGN KEY (article) REFERENCES public.yvs_base_articles(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_base_conditionnement yvs_base_conditionnement_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_conditionnement
    ADD CONSTRAINT yvs_base_conditionnement_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_conditionnement_point yvs_base_conditionnement_point_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_conditionnement_point
    ADD CONSTRAINT yvs_base_conditionnement_point_article_fkey FOREIGN KEY (article) REFERENCES public.yvs_base_article_point(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_base_conditionnement_point yvs_base_conditionnement_point_conditionnement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_conditionnement_point
    ADD CONSTRAINT yvs_base_conditionnement_point_conditionnement_fkey FOREIGN KEY (conditionnement) REFERENCES public.yvs_base_conditionnement(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_base_exercice yvs_base_exercice_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_exercice
    ADD CONSTRAINT yvs_base_exercice_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_famille_article yvs_base_famille_article_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_famille_article
    ADD CONSTRAINT yvs_base_famille_article_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_famille_article yvs_base_famille_article_famille_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_famille_article
    ADD CONSTRAINT yvs_base_famille_article_famille_parent_fkey FOREIGN KEY (famille_parent) REFERENCES public.yvs_base_famille_article(id);


--
-- Name: yvs_base_famille_article yvs_base_famille_article_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_famille_article
    ADD CONSTRAINT yvs_base_famille_article_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_groupes_article yvs_base_groupes_article_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_groupes_article
    ADD CONSTRAINT yvs_base_groupes_article_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_groupes_article yvs_base_groupes_article_groupe_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_groupes_article
    ADD CONSTRAINT yvs_base_groupes_article_groupe_parent_fkey FOREIGN KEY (groupe_parent) REFERENCES public.yvs_base_groupes_article(id);


--
-- Name: yvs_base_groupes_article yvs_base_groupes_article_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_groupes_article
    ADD CONSTRAINT yvs_base_groupes_article_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_liaison_caisse yvs_base_liaison_caisse_caisse_cible_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_liaison_caisse
    ADD CONSTRAINT yvs_base_liaison_caisse_caisse_cible_fkey FOREIGN KEY (caisse_cible) REFERENCES public.yvs_base_caisse(id);


--
-- Name: yvs_base_liaison_caisse yvs_base_liaison_caisse_caisse_source_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_liaison_caisse
    ADD CONSTRAINT yvs_base_liaison_caisse_caisse_source_fkey FOREIGN KEY (caisse_source) REFERENCES public.yvs_base_caisse(id);


--
-- Name: yvs_base_mode_reglement yvs_base_model_reglement_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_mode_reglement
    ADD CONSTRAINT yvs_base_model_reglement_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_model_reglement yvs_base_model_reglement_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_model_reglement
    ADD CONSTRAINT yvs_base_model_reglement_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_modele_reference yvs_base_modele_reference_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_modele_reference
    ADD CONSTRAINT yvs_base_modele_reference_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_modele_reference yvs_base_modele_reference_element_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_modele_reference
    ADD CONSTRAINT yvs_base_modele_reference_element_fkey FOREIGN KEY (element) REFERENCES public.yvs_base_element_reference(id);


--
-- Name: yvs_base_modele_reference yvs_base_modele_reference_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_modele_reference
    ADD CONSTRAINT yvs_base_modele_reference_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_plan_tarifaire yvs_base_plan_tarifaire_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire
    ADD CONSTRAINT yvs_base_plan_tarifaire_article_fkey FOREIGN KEY (article) REFERENCES public.yvs_base_articles(id);


--
-- Name: yvs_base_plan_tarifaire yvs_base_plan_tarifaire_conditionnement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire
    ADD CONSTRAINT yvs_base_plan_tarifaire_conditionnement_fkey FOREIGN KEY (conditionnement) REFERENCES public.yvs_base_conditionnement(id);


--
-- Name: yvs_base_plan_tarifaire yvs_base_plan_tarifaire_famille_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire
    ADD CONSTRAINT yvs_base_plan_tarifaire_famille_fkey FOREIGN KEY (famille) REFERENCES public.yvs_base_famille_article(id);


--
-- Name: yvs_base_plan_tarifaire_tranche yvs_base_plan_tarifaire_tranche_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire_tranche
    ADD CONSTRAINT yvs_base_plan_tarifaire_tranche_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_plan_tarifaire_tranche yvs_base_plan_tarifaire_tranche_plan_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire_tranche
    ADD CONSTRAINT yvs_base_plan_tarifaire_tranche_plan_fkey FOREIGN KEY (plan) REFERENCES public.yvs_base_plan_tarifaire(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_base_point_livraison yvs_base_point_livraison_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_livraison
    ADD CONSTRAINT yvs_base_point_livraison_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_point_livraison yvs_base_point_livraison_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_livraison
    ADD CONSTRAINT yvs_base_point_livraison_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_point_livraison yvs_base_point_livraison_ville_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_livraison
    ADD CONSTRAINT yvs_base_point_livraison_ville_fkey FOREIGN KEY (ville) REFERENCES public.yvs_dictionnaire(id);


--
-- Name: yvs_base_point_vente yvs_base_point_vente_agence_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_vente
    ADD CONSTRAINT yvs_base_point_vente_agence_fkey FOREIGN KEY (agence) REFERENCES public.yvs_agences(id);


--
-- Name: yvs_base_point_vente yvs_base_point_vente_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_vente
    ADD CONSTRAINT yvs_base_point_vente_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_point_vente_depot yvs_base_point_vente_depot_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_vente_depot
    ADD CONSTRAINT yvs_base_point_vente_depot_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_point_vente_depot yvs_base_point_vente_depot_depot_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_vente_depot
    ADD CONSTRAINT yvs_base_point_vente_depot_depot_fkey FOREIGN KEY (depot) REFERENCES public.yvs_base_depots(id);


--
-- Name: yvs_base_point_vente_depot yvs_base_point_vente_depot_point_vente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_point_vente_depot
    ADD CONSTRAINT yvs_base_point_vente_depot_point_vente_fkey FOREIGN KEY (point_vente) REFERENCES public.yvs_base_point_vente(id);


--
-- Name: yvs_base_article_categorie_comptable_taxe yvs_base_taxe_article_taxe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_article_categorie_comptable_taxe
    ADD CONSTRAINT yvs_base_taxe_article_taxe_fkey FOREIGN KEY (taxe) REFERENCES public.yvs_base_taxes(id);


--
-- Name: yvs_base_taxes yvs_base_taxes_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_taxes
    ADD CONSTRAINT yvs_base_taxes_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_tiers yvs_base_tiers_secteur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_tiers
    ADD CONSTRAINT yvs_base_tiers_secteur_fkey FOREIGN KEY (secteur) REFERENCES public.yvs_dictionnaire(id);


--
-- Name: yvs_base_tiers yvs_base_tiers_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_tiers
    ADD CONSTRAINT yvs_base_tiers_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_unite_mesure yvs_base_unite_mesure_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_unite_mesure
    ADD CONSTRAINT yvs_base_unite_mesure_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_users_acces yvs_base_users_acces_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_users_acces
    ADD CONSTRAINT yvs_base_users_acces_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_users_acces yvs_base_users_acces_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_users_acces
    ADD CONSTRAINT yvs_base_users_acces_code_fkey FOREIGN KEY (code) REFERENCES public.yvs_base_code_acces(id);


--
-- Name: yvs_base_users_acces yvs_base_users_acces_users_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_users_acces
    ADD CONSTRAINT yvs_base_users_acces_users_fkey FOREIGN KEY (users) REFERENCES public.yvs_users(id);


--
-- Name: yvs_base_categorie_client yvs_com_categorie_client_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_categorie_client
    ADD CONSTRAINT yvs_com_categorie_client_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_categorie_client yvs_com_categorie_client_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_categorie_client
    ADD CONSTRAINT yvs_com_categorie_client_parent_fkey FOREIGN KEY (parent) REFERENCES public.yvs_base_categorie_client(id);


--
-- Name: yvs_base_categorie_client yvs_com_categorie_client_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_categorie_client
    ADD CONSTRAINT yvs_com_categorie_client_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_com_categorie_tarifaire yvs_com_categorie_tarifaire_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_categorie_tarifaire
    ADD CONSTRAINT yvs_com_categorie_tarifaire_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_categorie_tarifaire yvs_com_categorie_tarifaire_categorie_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_categorie_tarifaire
    ADD CONSTRAINT yvs_com_categorie_tarifaire_categorie_fkey FOREIGN KEY (categorie) REFERENCES public.yvs_base_categorie_client(id);


--
-- Name: yvs_base_plan_tarifaire yvs_com_categorie_tarifaire_client_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire
    ADD CONSTRAINT yvs_com_categorie_tarifaire_client_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_plan_tarifaire yvs_com_categorie_tarifaire_client_categorie_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_plan_tarifaire
    ADD CONSTRAINT yvs_com_categorie_tarifaire_client_categorie_fkey FOREIGN KEY (categorie) REFERENCES public.yvs_base_categorie_client(id);


--
-- Name: yvs_com_categorie_tarifaire yvs_com_categorie_tarifaire_client_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_categorie_tarifaire
    ADD CONSTRAINT yvs_com_categorie_tarifaire_client_fkey FOREIGN KEY (client) REFERENCES public.yvs_com_client(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_com_client yvs_com_client_categorie_comptable_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_client
    ADD CONSTRAINT yvs_com_client_categorie_comptable_fkey FOREIGN KEY (categorie_comptable) REFERENCES public.yvs_base_categorie_comptable(id);


--
-- Name: yvs_com_client yvs_com_client_create_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_client
    ADD CONSTRAINT yvs_com_client_create_by_fkey FOREIGN KEY (create_by) REFERENCES public.yvs_users(id);


--
-- Name: yvs_com_client yvs_com_client_ligne_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_client
    ADD CONSTRAINT yvs_com_client_ligne_fkey FOREIGN KEY (ligne) REFERENCES public.yvs_base_point_livraison(id);


--
-- Name: yvs_com_client yvs_com_client_model_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_client
    ADD CONSTRAINT yvs_com_client_model_fkey FOREIGN KEY (model) REFERENCES public.yvs_base_model_reglement(id);


--
-- Name: yvs_com_client yvs_com_client_plan_ristourne_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_client
    ADD CONSTRAINT yvs_com_client_plan_ristourne_fkey FOREIGN KEY (plan_ristourne) REFERENCES public.yvs_com_plan_ristourne(id);


--
-- Name: yvs_com_client yvs_com_client_tiers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_client
    ADD CONSTRAINT yvs_com_client_tiers_fkey FOREIGN KEY (tiers) REFERENCES public.yvs_base_tiers(id);


--
-- Name: yvs_com_comerciale yvs_com_comerciale_agence_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_comerciale
    ADD CONSTRAINT yvs_com_comerciale_agence_fkey FOREIGN KEY (agence) REFERENCES public.yvs_agences(id);


--
-- Name: yvs_com_comerciale yvs_com_comerciale_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_comerciale
    ADD CONSTRAINT yvs_com_comerciale_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_comerciale yvs_com_comerciale_tiers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_comerciale
    ADD CONSTRAINT yvs_com_comerciale_tiers_fkey FOREIGN KEY (tiers) REFERENCES public.yvs_base_tiers(id);


--
-- Name: yvs_com_comerciale yvs_com_comerciale_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_comerciale
    ADD CONSTRAINT yvs_com_comerciale_utilisateur_fkey FOREIGN KEY (utilisateur) REFERENCES public.yvs_users(id);


--
-- Name: yvs_com_commercial_point yvs_com_commercial_point_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_commercial_point
    ADD CONSTRAINT yvs_com_commercial_point_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_commercial_point yvs_com_commercial_point_commercial_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_commercial_point
    ADD CONSTRAINT yvs_com_commercial_point_commercial_fkey FOREIGN KEY (commercial) REFERENCES public.yvs_com_comerciale(id);


--
-- Name: yvs_com_commercial_point yvs_com_commercial_point_point_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_commercial_point
    ADD CONSTRAINT yvs_com_commercial_point_point_fkey FOREIGN KEY (point) REFERENCES public.yvs_base_point_vente(id);


--
-- Name: yvs_com_commercial_vente yvs_com_commercial_vente_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_commercial_vente
    ADD CONSTRAINT yvs_com_commercial_vente_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_commercial_vente yvs_com_commercial_vente_commercial_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_commercial_vente
    ADD CONSTRAINT yvs_com_commercial_vente_commercial_fkey FOREIGN KEY (commercial) REFERENCES public.yvs_com_comerciale(id);


--
-- Name: yvs_com_commercial_vente yvs_com_commercial_vente_facture_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_commercial_vente
    ADD CONSTRAINT yvs_com_commercial_vente_facture_fkey FOREIGN KEY (facture) REFERENCES public.yvs_com_doc_ventes(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_base_conditionnement_point yvs_com_contenu_doc_stock_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_conditionnement_point
    ADD CONSTRAINT yvs_com_contenu_doc_stock_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_contenu_doc_vente yvs_com_contenu_doc_vente_article_bonus_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_contenu_doc_vente
    ADD CONSTRAINT yvs_com_contenu_doc_vente_article_bonus_fkey FOREIGN KEY (article_bonus) REFERENCES public.yvs_base_articles(id);


--
-- Name: yvs_com_contenu_doc_vente yvs_com_contenu_doc_vente_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_contenu_doc_vente
    ADD CONSTRAINT yvs_com_contenu_doc_vente_article_fkey FOREIGN KEY (article) REFERENCES public.yvs_base_articles(id);


--
-- Name: yvs_com_contenu_doc_vente yvs_com_contenu_doc_vente_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_contenu_doc_vente
    ADD CONSTRAINT yvs_com_contenu_doc_vente_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_contenu_doc_vente yvs_com_contenu_doc_vente_conditionnement_bonus_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_contenu_doc_vente
    ADD CONSTRAINT yvs_com_contenu_doc_vente_conditionnement_bonus_fkey FOREIGN KEY (conditionnement_bonus) REFERENCES public.yvs_base_conditionnement(id);


--
-- Name: yvs_com_contenu_doc_vente yvs_com_contenu_doc_vente_conditionnement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_contenu_doc_vente
    ADD CONSTRAINT yvs_com_contenu_doc_vente_conditionnement_fkey FOREIGN KEY (conditionnement) REFERENCES public.yvs_base_conditionnement(id);


--
-- Name: yvs_com_contenu_doc_vente yvs_com_contenu_doc_vente_depot_livraison_prevu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_contenu_doc_vente
    ADD CONSTRAINT yvs_com_contenu_doc_vente_depot_livraison_prevu_fkey FOREIGN KEY (depot_livraison_prevu) REFERENCES public.yvs_base_depots(id);


--
-- Name: yvs_com_contenu_doc_vente yvs_com_contenu_doc_vente_doc_vente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_contenu_doc_vente
    ADD CONSTRAINT yvs_com_contenu_doc_vente_doc_vente_fkey FOREIGN KEY (doc_vente) REFERENCES public.yvs_com_doc_ventes(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_com_contenu_doc_vente yvs_com_contenu_doc_vente_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_contenu_doc_vente
    ADD CONSTRAINT yvs_com_contenu_doc_vente_parent_fkey FOREIGN KEY (parent) REFERENCES public.yvs_com_contenu_doc_vente(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: yvs_com_creneau_point yvs_com_creneau_depot_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_point
    ADD CONSTRAINT yvs_com_creneau_depot_fkey FOREIGN KEY (point) REFERENCES public.yvs_base_point_vente(id);


--
-- Name: yvs_com_creneau_depot yvs_com_creneau_depot_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_depot
    ADD CONSTRAINT yvs_com_creneau_depot_fkey FOREIGN KEY (depot) REFERENCES public.yvs_base_depots(id);


--
-- Name: yvs_com_creneau_horaire_users yvs_com_creneau_horaire_employe_creneau_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_horaire_users
    ADD CONSTRAINT yvs_com_creneau_horaire_employe_creneau_fkey FOREIGN KEY (creneau_depot) REFERENCES public.yvs_com_creneau_depot(id);


--
-- Name: yvs_com_creneau_horaire_users yvs_com_creneau_horaire_point_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_horaire_users
    ADD CONSTRAINT yvs_com_creneau_horaire_point_fkey FOREIGN KEY (creneau_point) REFERENCES public.yvs_com_creneau_point(id);


--
-- Name: yvs_com_creneau_depot yvs_com_creneau_horaire_tranche_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_depot
    ADD CONSTRAINT yvs_com_creneau_horaire_tranche_fkey FOREIGN KEY (tranche) REFERENCES public.yvs_grh_tranche_horaire(id);


--
-- Name: yvs_com_creneau_horaire_users yvs_com_creneau_horaire_users_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_horaire_users
    ADD CONSTRAINT yvs_com_creneau_horaire_users_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_creneau_horaire_users yvs_com_creneau_horaire_users_users_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_horaire_users
    ADD CONSTRAINT yvs_com_creneau_horaire_users_users_fkey FOREIGN KEY (users) REFERENCES public.yvs_users(id);


--
-- Name: yvs_com_creneau_point yvs_com_creneau_point_tranche_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_creneau_point
    ADD CONSTRAINT yvs_com_creneau_point_tranche_fkey FOREIGN KEY (tranche) REFERENCES public.yvs_grh_tranche_horaire(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_adresse_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_adresse_fkey FOREIGN KEY (adresse) REFERENCES public.yvs_dictionnaire(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_annuler_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_annuler_by_fkey FOREIGN KEY (annuler_by) REFERENCES public.yvs_users(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_categorie_comptable_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_categorie_comptable_fkey FOREIGN KEY (categorie_comptable) REFERENCES public.yvs_base_categorie_comptable(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_client_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_client_fkey FOREIGN KEY (client) REFERENCES public.yvs_com_client(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_cloturer_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_cloturer_by_fkey FOREIGN KEY (cloturer_by) REFERENCES public.yvs_users(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_depot_livrer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_depot_livrer_fkey FOREIGN KEY (depot_livrer) REFERENCES public.yvs_base_depots(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_document_lie_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_document_lie_fkey FOREIGN KEY (document_lie) REFERENCES public.yvs_com_doc_ventes(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_entete_doc_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_entete_doc_fkey FOREIGN KEY (entete_doc) REFERENCES public.yvs_com_entete_doc_vente(id) ON DELETE CASCADE;


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_livreur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_livreur_fkey FOREIGN KEY (livreur) REFERENCES public.yvs_users(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_model_reglement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_model_reglement_fkey FOREIGN KEY (model_reglement) REFERENCES public.yvs_base_model_reglement(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_operateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_operateur_fkey FOREIGN KEY (operateur) REFERENCES public.yvs_users(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_tiers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_tiers_fkey FOREIGN KEY (tiers) REFERENCES public.yvs_com_client(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_tranche_livrer_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_tranche_livrer_fkey FOREIGN KEY (tranche_livrer) REFERENCES public.yvs_grh_tranche_horaire(id);


--
-- Name: yvs_com_doc_ventes yvs_com_doc_ventes_valider_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_doc_ventes
    ADD CONSTRAINT yvs_com_doc_ventes_valider_by_fkey FOREIGN KEY (valider_by) REFERENCES public.yvs_users(id);


--
-- Name: yvs_com_entete_doc_vente yvs_com_entete_doc_vente_agence_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_entete_doc_vente
    ADD CONSTRAINT yvs_com_entete_doc_vente_agence_fkey FOREIGN KEY (agence) REFERENCES public.yvs_agences(id);


--
-- Name: yvs_com_entete_doc_vente yvs_com_entete_doc_vente_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_entete_doc_vente
    ADD CONSTRAINT yvs_com_entete_doc_vente_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_entete_doc_vente yvs_com_entete_doc_vente_cloturer_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_entete_doc_vente
    ADD CONSTRAINT yvs_com_entete_doc_vente_cloturer_by_fkey FOREIGN KEY (cloturer_by) REFERENCES public.yvs_users(id);


--
-- Name: yvs_com_entete_doc_vente yvs_com_entete_doc_vente_creneau_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_entete_doc_vente
    ADD CONSTRAINT yvs_com_entete_doc_vente_creneau_fkey FOREIGN KEY (creneau) REFERENCES public.yvs_com_creneau_horaire_users(id);


--
-- Name: yvs_com_entete_doc_vente yvs_com_entete_doc_vente_valider_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_entete_doc_vente
    ADD CONSTRAINT yvs_com_entete_doc_vente_valider_by_fkey FOREIGN KEY (valider_by) REFERENCES public.yvs_users(id);


--
-- Name: yvs_com_grille_remise yvs_com_grille_remise_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_grille_remise
    ADD CONSTRAINT yvs_com_grille_remise_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_grille_remise yvs_com_grille_remise_remise_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_grille_remise
    ADD CONSTRAINT yvs_com_grille_remise_remise_fkey FOREIGN KEY (remise) REFERENCES public.yvs_com_remise(id);


--
-- Name: yvs_com_grille_ristourne yvs_com_grille_ristourne_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_grille_ristourne
    ADD CONSTRAINT yvs_com_grille_ristourne_article_fkey FOREIGN KEY (article) REFERENCES public.yvs_base_articles(id);


--
-- Name: yvs_com_grille_ristourne yvs_com_grille_ristourne_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_grille_ristourne
    ADD CONSTRAINT yvs_com_grille_ristourne_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_grille_ristourne yvs_com_grille_ristourne_conditionnement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_grille_ristourne
    ADD CONSTRAINT yvs_com_grille_ristourne_conditionnement_fkey FOREIGN KEY (conditionnement) REFERENCES public.yvs_base_conditionnement(id);


--
-- Name: yvs_com_grille_ristourne yvs_com_grille_ristourne_ristourne_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_grille_ristourne
    ADD CONSTRAINT yvs_com_grille_ristourne_ristourne_fkey FOREIGN KEY (ristourne) REFERENCES public.yvs_com_ristourne(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_com_parametre yvs_com_parametre_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_parametre
    ADD CONSTRAINT yvs_com_parametre_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_parametre yvs_com_parametre_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_parametre
    ADD CONSTRAINT yvs_com_parametre_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_com_parametre_vente yvs_com_parametre_vente_agence_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_parametre_vente
    ADD CONSTRAINT yvs_com_parametre_vente_agence_fkey FOREIGN KEY (agence) REFERENCES public.yvs_agences(id);


--
-- Name: yvs_com_parametre_vente yvs_com_parametre_vente_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_parametre_vente
    ADD CONSTRAINT yvs_com_parametre_vente_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_plan_ristourne yvs_com_plan_ristourne_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_plan_ristourne
    ADD CONSTRAINT yvs_com_plan_ristourne_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_com_rabais yvs_com_rabais_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_rabais
    ADD CONSTRAINT yvs_com_rabais_article_fkey FOREIGN KEY (article) REFERENCES public.yvs_base_conditionnement_point(id) ON DELETE CASCADE;


--
-- Name: yvs_com_remise yvs_com_remise_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_remise
    ADD CONSTRAINT yvs_com_remise_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_remise yvs_com_remise_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_remise
    ADD CONSTRAINT yvs_com_remise_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_com_ristourne yvs_com_ristourne_article_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_ristourne
    ADD CONSTRAINT yvs_com_ristourne_article_fkey FOREIGN KEY (article) REFERENCES public.yvs_base_articles(id);


--
-- Name: yvs_com_ristourne yvs_com_ristourne_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_ristourne
    ADD CONSTRAINT yvs_com_ristourne_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_ristourne yvs_com_ristourne_conditionnement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_ristourne
    ADD CONSTRAINT yvs_com_ristourne_conditionnement_fkey FOREIGN KEY (conditionnement) REFERENCES public.yvs_base_conditionnement(id);


--
-- Name: yvs_com_ristourne yvs_com_ristourne_famille_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_ristourne
    ADD CONSTRAINT yvs_com_ristourne_famille_fkey FOREIGN KEY (famille) REFERENCES public.yvs_base_famille_article(id);


--
-- Name: yvs_com_ristourne yvs_com_ristourne_plan_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_ristourne
    ADD CONSTRAINT yvs_com_ristourne_plan_fkey FOREIGN KEY (plan) REFERENCES public.yvs_com_plan_ristourne(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_com_taxe_contenu_vente yvs_com_taxe_contenu_vente_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_taxe_contenu_vente
    ADD CONSTRAINT yvs_com_taxe_contenu_vente_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_com_taxe_contenu_vente yvs_com_taxe_contenu_vente_contenu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_taxe_contenu_vente
    ADD CONSTRAINT yvs_com_taxe_contenu_vente_contenu_fkey FOREIGN KEY (contenu) REFERENCES public.yvs_com_contenu_doc_vente(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: yvs_com_taxe_contenu_vente yvs_com_taxe_contenu_vente_taxe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_com_taxe_contenu_vente
    ADD CONSTRAINT yvs_com_taxe_contenu_vente_taxe_fkey FOREIGN KEY (taxe) REFERENCES public.yvs_base_taxes(id);


--
-- Name: yvs_compta_acompte_client yvs_compta_acompte_client_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_acompte_client
    ADD CONSTRAINT yvs_compta_acompte_client_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_compta_acompte_client yvs_compta_acompte_client_caisse_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_acompte_client
    ADD CONSTRAINT yvs_compta_acompte_client_caisse_fkey FOREIGN KEY (caisse) REFERENCES public.yvs_base_caisse(id);


--
-- Name: yvs_compta_acompte_client yvs_compta_acompte_client_client_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_acompte_client
    ADD CONSTRAINT yvs_compta_acompte_client_client_fkey FOREIGN KEY (client) REFERENCES public.yvs_com_client(id);


--
-- Name: yvs_compta_acompte_client yvs_compta_acompte_client_model_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_acompte_client
    ADD CONSTRAINT yvs_compta_acompte_client_model_fkey FOREIGN KEY (model) REFERENCES public.yvs_base_mode_reglement(id);


--
-- Name: yvs_compta_caisse_piece_vente yvs_compta_caisse_piece_vente_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_vente
    ADD CONSTRAINT yvs_compta_caisse_piece_vente_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_compta_caisse_piece_vente yvs_compta_caisse_piece_vente_caisse_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_vente
    ADD CONSTRAINT yvs_compta_caisse_piece_vente_caisse_fkey FOREIGN KEY (caisse) REFERENCES public.yvs_base_caisse(id);


--
-- Name: yvs_compta_caisse_piece_vente yvs_compta_caisse_piece_vente_model_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_vente
    ADD CONSTRAINT yvs_compta_caisse_piece_vente_model_fkey FOREIGN KEY (model) REFERENCES public.yvs_base_mode_reglement(id) ON DELETE CASCADE;


--
-- Name: yvs_compta_caisse_piece_vente yvs_compta_caisse_piece_vente_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_vente
    ADD CONSTRAINT yvs_compta_caisse_piece_vente_parent_fkey FOREIGN KEY (parent) REFERENCES public.yvs_compta_caisse_piece_vente(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_compta_caisse_piece_vente yvs_compta_caisse_piece_vente_valide_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_vente
    ADD CONSTRAINT yvs_compta_caisse_piece_vente_valide_by_fkey FOREIGN KEY (valide_by) REFERENCES public.yvs_users(id);


--
-- Name: yvs_compta_caisse_piece_vente yvs_compta_caisse_piece_vente_vente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_vente
    ADD CONSTRAINT yvs_compta_caisse_piece_vente_vente_fkey FOREIGN KEY (vente) REFERENCES public.yvs_com_doc_ventes(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_compta_caisse_piece_virement yvs_compta_caisse_piece_virement_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_virement
    ADD CONSTRAINT yvs_compta_caisse_piece_virement_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_compta_caisse_piece_virement yvs_compta_caisse_piece_virement_caissier_source_cible_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_virement
    ADD CONSTRAINT yvs_compta_caisse_piece_virement_caissier_source_cible_fkey FOREIGN KEY (caissier_cible) REFERENCES public.yvs_users(id) ON DELETE SET NULL;


--
-- Name: yvs_compta_caisse_piece_virement yvs_compta_caisse_piece_virement_caissier_source_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_virement
    ADD CONSTRAINT yvs_compta_caisse_piece_virement_caissier_source_fkey FOREIGN KEY (caissier_source) REFERENCES public.yvs_users(id) ON DELETE SET NULL;


--
-- Name: yvs_compta_caisse_piece_virement yvs_compta_caisse_piece_virement_cible_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_virement
    ADD CONSTRAINT yvs_compta_caisse_piece_virement_cible_fkey FOREIGN KEY (cible) REFERENCES public.yvs_base_caisse(id);


--
-- Name: yvs_compta_caisse_piece_virement yvs_compta_caisse_piece_virement_model_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_virement
    ADD CONSTRAINT yvs_compta_caisse_piece_virement_model_fkey FOREIGN KEY (model) REFERENCES public.yvs_base_mode_reglement(id);


--
-- Name: yvs_compta_caisse_piece_virement yvs_compta_caisse_piece_virement_source_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_virement
    ADD CONSTRAINT yvs_compta_caisse_piece_virement_source_fkey FOREIGN KEY (source) REFERENCES public.yvs_base_caisse(id);


--
-- Name: yvs_compta_notif_reglement_vente yvs_compta_notif_reglement_vente_acompte_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_notif_reglement_vente
    ADD CONSTRAINT yvs_compta_notif_reglement_vente_acompte_fkey FOREIGN KEY (acompte) REFERENCES public.yvs_compta_acompte_client(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_compta_notif_reglement_vente yvs_compta_notif_reglement_vente_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_notif_reglement_vente
    ADD CONSTRAINT yvs_compta_notif_reglement_vente_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_compta_notif_reglement_vente yvs_compta_notif_reglement_vente_piece_vente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_notif_reglement_vente
    ADD CONSTRAINT yvs_compta_notif_reglement_vente_piece_vente_fkey FOREIGN KEY (piece_vente) REFERENCES public.yvs_compta_caisse_piece_vente(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_compta_parametre yvs_compta_parametre_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_parametre
    ADD CONSTRAINT yvs_compta_parametre_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_compta_parametre yvs_compta_parametre_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_parametre
    ADD CONSTRAINT yvs_compta_parametre_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_compta_caisse_piece_vente yvs_compta_piece_caisse_mission_caissier_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_compta_caisse_piece_vente
    ADD CONSTRAINT yvs_compta_piece_caisse_mission_caissier_fkey FOREIGN KEY (caissier) REFERENCES public.yvs_users(id);


--
-- Name: yvs_base_depots yvs_depots_agence_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_depots
    ADD CONSTRAINT yvs_depots_agence_fkey FOREIGN KEY (agence) REFERENCES public.yvs_agences(id);


--
-- Name: yvs_dictionnaire yvs_dictionnaire_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_dictionnaire
    ADD CONSTRAINT yvs_dictionnaire_parent_fkey FOREIGN KEY (parent) REFERENCES public.yvs_dictionnaire(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: yvs_grh_tranche_horaire yvs_grh_tranche_horaire_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_grh_tranche_horaire
    ADD CONSTRAINT yvs_grh_tranche_horaire_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_model_reglement yvs_model_de_reglement_societe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_model_reglement
    ADD CONSTRAINT yvs_model_de_reglement_societe FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_mode_reglement yvs_model_de_reglement_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_mode_reglement
    ADD CONSTRAINT yvs_model_de_reglement_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_exercice yvs_mut_exercice_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_exercice
    ADD CONSTRAINT yvs_mut_exercice_author_fkey FOREIGN KEY (author) REFERENCES public.yvs_users_agence(id);


--
-- Name: yvs_base_conditionnement yvs_prod_conditionnement_conditionnement_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_conditionnement
    ADD CONSTRAINT yvs_prod_conditionnement_conditionnement_fkey FOREIGN KEY (unite) REFERENCES public.yvs_base_unite_mesure(id);


--
-- Name: yvs_base_unite_mesure yvs_prod_unite_masse_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_unite_mesure
    ADD CONSTRAINT yvs_prod_unite_masse_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_synchro_data_synchro yvs_synchro_data_synchro_id_listen_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_synchro_data_synchro
    ADD CONSTRAINT yvs_synchro_data_synchro_id_listen_fkey FOREIGN KEY (id_listen) REFERENCES public.yvs_synchro_listen_table(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: yvs_synchro_data_synchro yvs_synchro_data_synchro_serveur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_synchro_data_synchro
    ADD CONSTRAINT yvs_synchro_data_synchro_serveur_fkey FOREIGN KEY (serveur) REFERENCES public.yvs_synchro_serveurs(id);


--
-- Name: yvs_synchro_listen_table yvs_synchro_listen_table_serveur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_synchro_listen_table
    ADD CONSTRAINT yvs_synchro_listen_table_serveur_fkey FOREIGN KEY (serveur) REFERENCES public.yvs_synchro_serveurs(id);


--
-- Name: yvs_base_taxes yvs_taxes_societe_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_taxes
    ADD CONSTRAINT yvs_taxes_societe_fkey FOREIGN KEY (societe) REFERENCES public.yvs_societes(id);


--
-- Name: yvs_base_tiers yvs_tiers_ville_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_base_tiers
    ADD CONSTRAINT yvs_tiers_ville_fkey FOREIGN KEY (ville) REFERENCES public.yvs_dictionnaire(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: yvs_users_agence yvs_users_agence_agence_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_users_agence
    ADD CONSTRAINT yvs_users_agence_agence_fkey FOREIGN KEY (agence) REFERENCES public.yvs_agences(id);


--
-- Name: yvs_users yvs_users_agence_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_users
    ADD CONSTRAINT yvs_users_agence_fkey FOREIGN KEY (agence) REFERENCES public.yvs_agences(id);


--
-- Name: yvs_users_agence yvs_users_agence_users_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.yvs_users_agence
    ADD CONSTRAINT yvs_users_agence_users_fkey FOREIGN KEY (users) REFERENCES public.yvs_users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--
