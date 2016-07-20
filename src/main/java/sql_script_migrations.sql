/**

	MAKE COPIES OF THE CURRENT DB AND UPDATE DOID TO disease_id WHERE APPROPRIATE.

**/


/* insert new species into species table */

insert into COPADB_LOCAL.species (species_name) values ("Rat");
insert into COPADB_LOCAL.species (species_name) values ("Zebrafish");


/* creates new disease table */

create table COPADB_LOCAL.disease_new select * from COPADB_LOCAL.disease;
alter table COPADB_LOCAL.disease_new change DOID disease_id varchar(100);
alter table COPADB_LOCAL.disease_new add primary key (disease_id);
rename table COPADB_LOCAL.disease to COPADB_LOCAL.disease_old;
rename table COPADB_LOCAL.disease_new to COPADB_LOCAL.disease;


/* creates new disease_gene table */

create table COPADB_LOCAL.disease_gene_new select * from COPADB_LOCAL.disease_gene;
alter table COPADB_LOCAL.disease_gene_new change DOID disease_id varchar(100);
alter table COPADB_LOCAL.disease_gene_new add primary key (disease_id, ensembl_id);
alter table COPADB_LOCAL.disease_gene_new add foreign key (disease_id) references COPADB_LOCAL.disease(disease_id);
alter table COPADB_LOCAL.disease_gene_new add foreign key (ensembl_id) references COPADB_LOCAL.gene(ensembl_id);
rename table COPADB_LOCAL.disease_gene to COPADB_LOCAL.disease_gene_old;
rename table COPADB_LOCAL.disease_gene_new to COPADB_LOCAL.disease_gene;


/* creates new disease_gene_publication table that maps disease-gene pairs to publications they appeared in */
create table COPADB_LOCAL.disease_gene_publication
  select disease_id, ensembl_id, pubmed_id, pubmed_title, pubmed_author from COPADB_LOCAL.disease_gene;
alter table COPADB_LOCAL.disease_gene_publication add id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT;
alter table COPADB_LOCAL.disease_gene_publication add foreign key (disease_id, ensembl_id)
references COPADB_LOCAL.disease_gene(disease_id, ensembl_id);

/* drop migrated fields from disease_gene, which now appear in disease_gene_publications */
alter table COPADB_LOCAL.disease_gene drop pubmed_id, drop pubmed_title, drop pubmed_author;

/* last step, add new fields */

alter table COPADB_LOCAL.disease add notes varchar(500) default "";
alter table COPADB_LOCAL.disease add disease_url varchar(200) default "";
alter table COPADB_LOCAL.disease_gene add relationship varchar(100) default "";
alter table COPADB_LOCAL.disease_gene add weblink varchar(200) default "";
alter table COPADB_LOCAL.disease_gene add data_source varchar(100) default "";

update COPADB_LOCAL.disease_gene set data_source = 'COPaKB' where 1=1;


/* Note! to reverse any changes to the gene table, simple drop all rows with chromosome == NULL, or make a copy gene_old */