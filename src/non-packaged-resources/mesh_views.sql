
CREATE MATERIALIZED VIEW mesh_staging.descriptor AS
SELECT
    ui,
    name,
    xml_pretty(tree_number_list) as tree_number_list
FROM mesh_staging.raw_desc,
     LATERAL XMLTABLE('/DescriptorRecord' PASSING xml COLUMNS
                        ui text PATH ('DescriptorUI'),
                        name text PATH ('DescriptorName/String'),
                        tree_number_list xml PATH ('TreeNumberList')
                      )
;

CREATE MATERIALIZED VIEW mesh_staging.tree_number AS
SELECT
    ui,
    seqnum,
    tree_number
FROM mesh_staging.descriptor,
     LATERAL XMLTABLE('/TreeNumberList/TreeNumber' PASSING tree_number_list COLUMNS
                        seqnum FOR ORDINALITY,
                        tree_number text PATH ('.')
                      )
;
