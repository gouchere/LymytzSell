-- get_stock_reel(art_ bigint, tranche_ bigint, depot_ bigint, agence_ bigint, societe_ bigint, date_ date, unite_ bigint, lot_ bigint)
-- PAIN 135 - GUICHET PAIN
select get_stock_reel(9314, null, 1931, 0, 0, current_date, 2515, 0);
-- PAIN 125
select get_stock_reel(7369, null, 1931, 0, 0, current_date, 122, 0);
-- PAIN 100
select get_stock_reel(7370, null, 1931, 0, 0, current_date, 121, 0);
-- PAIN 50
select get_stock_reel(7449, null, 1931, 0, 0, current_date, 1731, 0);

-- GUICHET MARCHANDISES
select get_stock_reel(8227, null, 1923, 0, 0, current_date, 974, 0);

-- les 100 articles les plus mouvementés
select m.article, m.conditionnement, SUM(m.quantite) total_quantite from yvs_base_mouvement_stock m
group by m.article, m.conditionnement
order by total_quantite desc
    limit 100 ;
