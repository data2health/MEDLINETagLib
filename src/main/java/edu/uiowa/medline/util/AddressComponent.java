package edu.uiowa.medline.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AddressComponent {
    static Pattern leadingZip = Pattern.compile("([A-Z]{1,2}[--])?[0-9]{4,6} (.*)$");
    static Pattern trailingPostal = Pattern.compile("(.*) ([A-Z]{1,2}[A-Z0-9][A-Z0-9] [0-9][A-Z][0-9A-Z])$");
    static Pattern trailingZip = Pattern.compile("(.*) ([0-9]{3,6}(-[0-9]{3,4})?)$");
    static Pattern trailingIndianZip = Pattern.compile("(.*)[- ][0-9][0-9][0-9]$");

    String label = null;
    String category = null;
    
    AddressComponent(String label) {
	Matcher leadingZipMatcher = leadingZip.matcher(label);
	if (leadingZipMatcher.matches()) {
	    this.label = leadingZipMatcher.group(2).trim();
	} else
	    this.label = label.trim();
	
	Matcher trailingZipMatcher = trailingZip.matcher(this.label);
	Matcher postalMatcher = trailingPostal.matcher(this.label);
	if (trailingZipMatcher.matches()) {
	    this.label = trailingZipMatcher.group(1).trim();
	} else if (postalMatcher.matches()) {
	    this.label = postalMatcher.group(1).trim();
	} else
	    this.label = label.trim();
	
	Matcher trailingIndianZipMatcher = trailingIndianZip.matcher(this.label);
	if (trailingIndianZipMatcher.matches()) {
	    this.label = trailingIndianZipMatcher.group(1).trim();
	}
	categorize();
    }
    
    public String toString() {
        return label;
    }
    
    void categorize() {
        fullCategorize();
        if (category == null)
    	prePostCategorize();
        if (category == null)
    	infixCategorize();
    }
	
	void fullCategorize() {
	    if (AddressTokenizer.countryHash.containsKey(label)) {
		category = "Country";
		return;
	    }
	    if (AddressTokenizer.stateHash.containsKey(label) && !AddressTokenizer.cityHash.containsKey(label)) {
		category = "State";
		return;
	    }
	    if (AddressTokenizer.cityHash.containsKey(label)) {
		category = "City";
		return;
	    }
	    if (AddressTokenizer.universityHash.containsKey(label)) {
		category = "University";
		return;
	    }
	    
	    if (category != null ) return;
	    
	    fullCategorize("The Netherlands", "Country");
	    fullCategorize("Korea", "Country");
	    fullCategorize("PR China", "Country");
	    fullCategorize("People's Republic of China", "Country");
	    fullCategorize("P.R. China", "Country");
	    fullCategorize("U.K", "Country");
	    fullCategorize("U. K", "Country");
	    fullCategorize("Federal Republic of Germany", "Country");
	    fullCategorize("P. R. China", "Country");
	    fullCategorize("Czech Republic", "Country");
	    fullCategorize("Republic of China", "Country");
	    fullCategorize("ROC", "Country");
	    fullCategorize("the Netherlands", "Country");
	    fullCategorize("FRG", "Country");
	    fullCategorize("R.O.C", "Country");
	    fullCategorize("Netherlands", "Country");
	    fullCategorize("F.R.G", "Country");
	    fullCategorize("IR Iran", "Country");
	    fullCategorize("Republic of Singapore", "Country");
	    fullCategorize("U.S.A", "Country");
	    fullCategorize("China (mainland)", "Country");
	    fullCategorize("GDR", "Country");
	    fullCategorize("Republic of South Africa", "Country");
	    fullCategorize("Yugoslavia", "Country");
	    fullCategorize("Czechoslovakia", "Country");
	    fullCategorize("USSR", "Country");
	    fullCategorize("P.R.China", "Country");
	    fullCategorize("West Indies", "Country");
	    fullCategorize("P R China", "Country");
	    fullCategorize("Fed. Rep. of Germany", "Country");
	    fullCategorize("Turkiye", "Country");
	    fullCategorize("I.R. Iran", "Country");
	    fullCategorize("People׳s Republic of China", "Country");
	    fullCategorize("P.R. China", "Country");
	    fullCategorize("The People's Republic of China", "Country");
	    fullCategorize("JAPAN", "Country");
	    fullCategorize("I. R. Iran", "Country");
	    fullCategorize("U.S.S.R", "Country");
	    fullCategorize("INDIA", "Country");
	    fullCategorize("P. R. China", "Country");
	    fullCategorize("G.D.R", "Country");
	    fullCategorize("Peoples Republic of China", "Country");
	    fullCategorize("R. Macedonia", "Country");
	    fullCategorize("German Democratic Republic", "Country");
	    fullCategorize("N. Ireland", "Country");
	    fullCategorize("West Africa", "Country");
	    fullCategorize("S. Korea", "Country");
	    fullCategorize("Peoples' Republic of China", "Country");
	    fullCategorize("DDR", "Country");
	    fullCategorize("Serbia and Montenegro", "Country");

	    fullCategorize("DC", "State");
	    fullCategorize("D.C", "State");
	    fullCategorize("D. C", "State");
	    fullCategorize("D.C.", "State");
	    fullCategorize("D. C.", "State");
	    fullCategorize("Ont", "State");
	    fullCategorize("Mass", "State");
	    fullCategorize("Md", "State");
	    fullCategorize("Victoria", "State");
	    fullCategorize("Calif", "State");
	    fullCategorize("Pa", "State");
	    fullCategorize("Tex", "State");
	    fullCategorize("Ill", "State");
	    fullCategorize("Nova Scotia", "State");
	    fullCategorize("Minn", "State");
	    fullCategorize("ON", "State");
	    fullCategorize("N.Y", "State");
	    fullCategorize("BC", "State");
	    fullCategorize("Mich", "State");
	    fullCategorize("Mo", "State");
	    fullCategorize("D.F", "State");
	    fullCategorize("SP", "State");
	    fullCategorize("Québec", "State");
	    fullCategorize("DF", "State");
	    fullCategorize("VIC", "State");
	    fullCategorize("N.Y.", "State");
	    fullCategorize("Vic", "State");
	    fullCategorize("Ga", "State");
	    fullCategorize("SA", "State");
	    fullCategorize("Fla", "State");
	    fullCategorize("N. Y", "State");
	    fullCategorize("NT", "State");
	    fullCategorize("N.C", "State");
	    fullCategorize("Colo", "State");
	    fullCategorize("Wash", "State");
	    fullCategorize("Ky", "State");
	    fullCategorize("Va", "State");
	    fullCategorize("Md.", "State");
	    fullCategorize("Mass.", "State");
	    fullCategorize("Wis", "State");
	    fullCategorize("VT", "State");
	    fullCategorize("La", "State");
	    fullCategorize("Ariz", "State");
	    fullCategorize("N.T", "State");
	    fullCategorize("New Territories", "State");
	    fullCategorize("Ont.", "State");
	    fullCategorize("Vic.", "State");
	    fullCategorize("D.F.", "State");
	    fullCategorize("Qld", "State");
	    fullCategorize("Calif.", "State");
	    fullCategorize("Que.", "State");
	    fullCategorize("UP", "State");
	    fullCategorize("Pa.", "State");
	    fullCategorize("Ill.", "State");
	    fullCategorize("N.S.W.", "State");
	    fullCategorize("B.C.", "State");
	    fullCategorize("Fla.", "State");
	    fullCategorize("Mich.", "State");
	    fullCategorize("Minn.", "State");
	    fullCategorize("Tex.", "State");
	    fullCategorize("U.P.", "State");
	    fullCategorize("Conn.", "State");
	    fullCategorize("Qld.", "State");
	    fullCategorize("Conn", "State");
	    fullCategorize("Mo.", "State");
	    fullCategorize("N.T.", "State");
	    fullCategorize("Ga.", "State");
	    fullCategorize("Qué.", "State");
	    fullCategorize("Tenn", "State");
	    fullCategorize("N.C.", "State");
	    fullCategorize("Wash.", "State");
	    fullCategorize("Tenn.", "State");
	    fullCategorize("Va.", "State");
	    fullCategorize("Co. Cork", "State");
	    fullCategorize("D. F.", "State");
	    fullCategorize("Wis.", "State");
	    fullCategorize("Ariz.", "State");
	    fullCategorize("N.J.", "State");
	    fullCategorize("Ind.", "State");
	    fullCategorize("Colo.", "State");
	    fullCategorize("Ind", "State");
	    fullCategorize("Sask.", "State");
	    fullCategorize("La.", "State");

	    fullCategorize("Hong Kong SAR", "City");
	    fullCategorize("Rehovot", "City");
	    fullCategorize("Neuherberg", "City");
	    fullCategorize("Martinsried", "City");
	    fullCategorize("Homburg/Saar", "City");
	    fullCategorize("Bellaterra", "City");
	    fullCategorize("Bilthoven", "City");
	    fullCategorize("Berkshire", "City");
	    fullCategorize("Tel-Hashomer", "City");
	    fullCategorize("Tygerberg", "City");
	    fullCategorize("Heverlee", "City");
	    fullCategorize("Safat", "City");
	    fullCategorize("Postbus", "City");
	    fullCategorize("Edgbaston", "City");
	    fullCategorize("Halle/Saale", "City");
	    fullCategorize("Wilrijk", "City");
	    fullCategorize("Warszawie", "City");
	    fullCategorize("Que", "City");
	    fullCategorize("Lodzi", "City");
	    fullCategorize("Lódź", "City");
	    fullCategorize("Krakowie", "City");
	    fullCategorize("Poznaniu", "City");
	    fullCategorize("Wrocławiu", "City");
	    fullCategorize("Katowicach", "City");
	    fullCategorize("Tel Hashomer", "City");
	    
	    fullCategorize("Universitätsklinik", "University");
	    fullCategorize("Univ", "University");
	    fullCategorize("Univer", "University");
	    
	    fullCategorize("Universitätsklinik", "Hospital");
	    fullCategorize("Universitätskliniken", "Hospital");

	    fullCategorize("INSERM", "IC");
	    fullCategorize("INFN", "IC");
	    fullCategorize("INRA", "IC");

	    fullCategorize("Assistant Professor", "Person");
	    fullCategorize("Professor", "Person");
	    fullCategorize("Associate Professor", "Person");
	    fullCategorize("Senior Lecturer", "Person");
	    fullCategorize("Director", "Person");
	    fullCategorize("Resident", "Person");
	    fullCategorize("Reader", "Person");
	    fullCategorize("Professor and Head", "Person");
	    fullCategorize("Lecturer", "Person");
	    fullCategorize("Editor", "Person");
	    fullCategorize("Editor-in-Chief", "Person");
	    fullCategorize("Faculty", "Person");
	    fullCategorize("Senior Resident", "Person");
	    fullCategorize("Resident", "Person");
	    fullCategorize("Student", "Person");
	    fullCategorize("President", "Person");
	}
	
	void prePostCategorize() {
	    prePostCategorize("Province", "State");

	    prePostCategorize("Department", "Department");
	    prePostCategorize("From the Department", "Department");
	    prePostCategorize("Dept.", "Department");
	    prePostCategorize("The Department", "Department");
	    prePostCategorize("First Department", "Department");
	    prePostCategorize("Second Department", "Department");
	    prePostCategorize("Third Department", "Department");
	    prePostCategorize("Fourth Department", "Department");
	    prePostCategorize("1st Department", "Department");
	    prePostCategorize("2nd Department", "Department");
	    prePostCategorize("3rd Department", "Department");
	    prePostCategorize("4th Department", "Department");
	    prePostCategorize("Departamento", "Department");
	    prePostCategorize("Dipartimento", "Department");
	    prePostCategorize("Servicio", "Department");
	    prePostCategorize("Service", "Department");
	    prePostCategorize("Serviço", "Department");
	    prePostCategorize("Unit", "Department");
	    prePostCategorize("Program", "Department");
	    prePostCategorize("Departament", "Department");
	    prePostCategorize("Departments", "Department");
	    prePostCategorize("Département", "Department");
	    prePostCategorize("Servizio", "Department");
	    prePostCategorize("Dept", "Department");
	    prePostCategorize("Department of Surgery", "Department");
	    prePostCategorize("Department of Urology", "Department");
	    prePostCategorize("Department of Obstetrics and Gynaecology", "Department");
	    prePostCategorize("Departement", "Department");
	    prePostCategorize("Department of Anaesthetics", "Department");
	    prePostCategorize("Department of Medical Research", "Department");
	    prePostCategorize("Department of Clinical Medicine", "Department");
	    prePostCategorize("Department of Biochemistry", "Department");
	    prePostCategorize("Department of Medical Oncology", "Department");
	    prePostCategorize("Dept. of Surgery", "Department");
	    prePostCategorize("Department of Biochemistry", "Department");
	    prePostCategorize("Department of Clinical Research", "Department");
	    prePostCategorize("Department of Chemical Engineering and Materials Science", "Department");
	    prePostCategorize("Department of Medicine", "Department");
	    prePostCategorize("Fachbereich", "Department");
	    prePostCategorize("Fakultät", "Department");

	    prePostCategorize("Division", "Division");
	    prePostCategorize("From the Division", "Division");
	    prePostCategorize("Div.", "Division");
	    prePostCategorize("Branch", "Division");
	    prePostCategorize("Section", "Division");
	    prePostCategorize("Divisione", "Division");
	    
	    prePostCategorize("Laboratory", "Laboratory");
	    prePostCategorize("Laboratories", "Laboratory");
	    prePostCategorize("Lab.", "Laboratory");
	    prePostCategorize("State Key Laboratory", "Laboratory");
	    prePostCategorize("Laboratoire", "Laboratory");
	    
	    prePostCategorize("Group", "Group ");
	    
	    prePostCategorize("Project", "Project ");
	    
	    prePostCategorize("College", "College");
	    prePostCategorize("College of Medicine", "College");
	    prePostCategorize("School", "College");
	    prePostCategorize("School of Medicine", "College");
	    prePostCategorize("Graduate School", "College");
	    prePostCategorize("Faculty", "College");
	    prePostCategorize("Faculdade", "College");
	    prePostCategorize("School of Public Health", "College");
	    prePostCategorize("Medical School", "College");
	    prePostCategorize("Medical College", "College");
	    prePostCategorize("Faculté de Médecine", "College");
	    prePostCategorize("College of Physicians and Surgeons", "College");
	    prePostCategorize("Faculty of Medicine", "College");
	    prePostCategorize("Faculté de", "College");
	    prePostCategorize("Faculté des", "College");
	    prePostCategorize("Facultad", "College");
	    prePostCategorize("Lehrstuhl", "College");

	    prePostCategorize("Institute", "IC");
	    prePostCategorize("Center", "IC");
	    prePostCategorize("Centers", "IC");
	    prePostCategorize("Centre", "IC");
	    prePostCategorize("Centro", "IC");
	    prePostCategorize("Istituto", "IC");
	    prePostCategorize("Institut", "IC");
	    prePostCategorize("Instituto", "IC");
	    prePostCategorize("Instituto", "IC");
	    prePostCategorize("Institute of Technology", "IC");
	    prePostCategorize("Institutet", "IC");
	    prePostCategorize("Medical Institutions", "IC");
	    prePostCategorize("Institutes for Biological Sciences", "IC");
	    prePostCategorize("Institute of Medical Sciences", "IC");
	    prePostCategorize("Institute of Research", "IC");
	    prePostCategorize("Institute of Chemical Physics", "IC");
	    prePostCategorize("Institute of Occupational Health", "IC");
	    prePostCategorize("Institute for Brain", "IC");
	    prePostCategorize("Institute of Medical Research", "IC");
	    prePostCategorize("Institute of Psychiatry", "IC");
	    prePostCategorize("Institute for Biomedical Research", "IC");
	    prePostCategorize("Institution of Oceanography", "IC");
	    prePostCategorize("Institute for Medical Research", "IC");
	    prePostCategorize("Institut für Biochemie", "IC");
	    prePostCategorize("Instituto de", "IC");
	    prePostCategorize("Centre for Human Genetics", "IC");
	    prePostCategorize("Institutes for BioMedical Research", "IC");
	    prePostCategorize("Institute of Molecular Biology", "IC");
	    prePostCategorize("Institute of Medical Research", "IC");
	    prePostCategorize("Institute of Bioorganic Chemistry", "IC");
	    prePostCategorize("Centre for Excellence in HIV/AIDS", "IC");
	    prePostCategorize("Centre for Epidemiology and Population Health", "IC");
	    prePostCategorize("Institute of Medical Research", "IC");
	    prePostCategorize("Institute of Public Health", "IC");
	    prePostCategorize("Institute for Human Cognitive and Brain Sciences", "IC");
	    prePostCategorize("Centre for Cell Biology", "IC");
	    prePostCategorize("Institute for Child Health Research", "IC");
	    prePostCategorize("Center for Environment and Health", "IC");
	    prePostCategorize("Institute of Clinical Medicine", "IC");
	    prePostCategorize("Institutes for Biomedical Research", "IC");
	    prePostCategorize("Institute of Materia Medica", "IC");
	    prePostCategorize("Institute of Dermatology", "IC");
	    prePostCategorize("Institute of Colloids and Interfaces", "IC");
	    prePostCategorize("Institute for Evolutionary Anthropology", "IC");
	    prePostCategorize("Institute of Mental Health", "IC");
	    prePostCategorize("Institute for Research and Evaluation", "IC");
	    prePostCategorize("Institute for Psychiatric and Behavioral Genetics", "IC");
	    prePostCategorize("Institute for Cancer Research", "IC");
	    prePostCategorize("Institute for Biomedical Research", "IC");
	    prePostCategorize("Institute of Radiology", "IC");
	    prePostCategorize("Institut für Biochemie", "IC");
	    prePostCategorize("Institutes for BioMedical Research", "IC");
	    prePostCategorize("Institut für Kohlenforschung", "IC");
	    prePostCategorize("Institute of Materia Medica", "IC");
	    prePostCategorize("Institute for Medical Research", "IC");
	    prePostCategorize("Institute of Hematology", "IC");
	    prePostCategorize("Institut für Molekulare Genetik", "IC");
	    prePostCategorize("Institute for Research in Cancer and Allied Diseases", "IC");
	    prePostCategorize("Institute of Molecular Biology", "IC");
	    prePostCategorize("Institute for Plant Research", "IC");
	    prePostCategorize("Institute of Medical Research", "IC");
	    prePostCategorize("Institute of Bioorganic Chemistry", "IC");
	    prePostCategorize("Institute of Radiation Medicine", "IC");
	    prePostCategorize("Institute of Bioscience and Biotechnology", "IC");
	    prePostCategorize("Centre for Genetic Engineering and Biotechnology", "IC");
	    prePostCategorize("Institute of Cardiovascular Disease", "IC");
	    prePostCategorize("Institute of Molecular Pathology", "IC");
	    prePostCategorize("Centre for Diarrhoeal Disease Research", "IC");
	    prePostCategorize("Institute of Rheumatology", "IC");
	    prePostCategorize("Institution of Washington", "IC");
	    prePostCategorize("INFN", "IC");

	    prePostCategorize("Hospital", "Hospital");
	    prePostCategorize("Clinic", "Hospital");
	    prePostCategorize("Klinik", "Hospital");
	    prePostCategorize("Universitätsspital", "Hospital");
	    prePostCategorize("Kantonsspital", "Hospital");
	    prePostCategorize("Universitätsklinikum", "Hospital");
	    prePostCategorize("Inselspital", "Hospital");
	    prePostCategorize("Infirmary", "Hospital");
	    prePostCategorize("International Medical Center", "Hospital");
	    prePostCategorize("Zentralklinikum", "Hospital");
	    prePostCategorize("Klinikum", "Hospital");
	    prePostCategorize("Royal Infirmary", "Hospital");
	    prePostCategorize("Hôpital", "Hospital");
	    prePostCategorize("Children's Hospital", "Hospital");
	    prePostCategorize("Klinicki", "Hospital");
	    prePostCategorize("Academisch Ziekenhuis", "Hospital");
	    prePostCategorize("Rigshospitalet", "Hospital");
	    prePostCategorize("Medizinische Klinik", "Hospital");
	    prePostCategorize("Universitetshospital", "Hospital");
	    prePostCategorize("Universitätskrankenhaus", "Hospital");
	    prePostCategorize("Universitätsmedizin", "Hospital");
	    prePostCategorize("Universitätsklinik", "Hospital");
	    prePostCategorize("Universitäts-Krankenhaus", "Hospital");
	    prePostCategorize("Universitätskliniken", "Hospital");
	    prePostCategorize("Universitätskrankenhauses", "Hospital");
	    prePostCategorize("Universitätsfrauenklinik", "Hospital");
	    prePostCategorize("Universitäts-Frauenklinik", "Hospital");
	    prePostCategorize("Universitäts-Augenklinik", "Hospital");
	    prePostCategorize("Universitätsaugenklinik", "Hospital");
	    prePostCategorize("Universitätskinderklinik", "Hospital");
	    prePostCategorize("Universitäts-Kinderklinik", "Hospital");
	    prePostCategorize("Universitetssjukhuset", "Hospital");
	    prePostCategorize("UniversitätsSpital", "Hospital");
	    prePostCategorize("Universitäts-Hautklinik", "Hospital");
	    prePostCategorize("Universitäts-Kinderspital", "Hospital");
	    prePostCategorize("Universitäts-HNO-Klinik", "Hospital");
	    prePostCategorize("Universitäts-Hals-Nasen-Ohrenklinik", "Hospital");
	    prePostCategorize("Universitätsspitals", "Hospital");
	    prePostCategorize("Universität-Augenklinik", "Hospital");
	    prePostCategorize("Universitäts Augenklinik", "Hospital");
	    prePostCategorize("Univers.-Frauenklinik", "Hospital");
	    prePostCategorize("Univ.-Klinik", "Hospital");
	    prePostCategorize("Univ.-Hautklinik", "Hospital");
	    prePostCategorize("Univ.-Augenklinik", "Hospital");
	    prePostCategorize("Universitäts-Hals-Nasen-Ohren-Klinik", "Hospital");
	    prePostCategorize("Univ.-HNO-Klinik", "Hospital");
	    prePostCategorize("Univers.-Klinik", "Hospital");
	    prePostCategorize("Universitäts-Hals-Nasen-Ohren-Klinik", "Hospital");

	    prePostCategorize("University", "University");
	    prePostCategorize("Universités", "University");
	    prePostCategorize("Université", "University");
	    prePostCategorize("State University", "University");
	    prePostCategorize("The University", "University");
	    prePostCategorize("Ludwig-Maximilians-Universität", "University");
	    prePostCategorize("Universität", "University");
	    prePostCategorize("Universidade", "University");
	    prePostCategorize("Università", "University");
	    prePostCategorize("Freie Universität", "University");
	    prePostCategorize("Medical University", "University");
	    prePostCategorize("Medizinischen Hochschule", "University");
	    prePostCategorize("Humboldt-Universität", "University");
	    prePostCategorize("Freien Universität", "University");
	    prePostCategorize("Technischen Universität", "University");
	    prePostCategorize("University of Medical Science", "University");
	    prePostCategorize("Friedrich-Schiller-Universität", "University");
	    prePostCategorize("Philipps-Universität", "University");
	    prePostCategorize("Academy of Sciences", "University");
	    prePostCategorize("Queen's University", "University");
	    prePostCategorize("King's University", "University");
	    prePostCategorize("Johannes-Gutenberg-Universität", "University");
	    prePostCategorize("School of Hygiene and Tropical Medicine", "University");
	    prePostCategorize("King's College", "University");
	    prePostCategorize("Universidad", "University");
	    prePostCategorize("Medizinische Universität", "University");
	    prePostCategorize("Ernst-Moritz-Arndt-Universität", "University");
	    prePostCategorize("Medical Academy", "University");
	    prePostCategorize("Medizinischen Akademie", "University");
	    prePostCategorize("Ruhr-Universität", "University");
	    prePostCategorize("Imperial College", "University");
	    prePostCategorize("University of Science and Technology", "University");
	    prePostCategorize("National University", "University");
	    prePostCategorize("Tech", "University");
	    prePostCategorize("Universitat Autònoma", "University");
	    prePostCategorize("Katholieke Universiteit", "University");
	    prePostCategorize("Academia", "University");
	    prePostCategorize("The State University", "University");
	    prePostCategorize("Universiti", "University");
	    prePostCategorize("University of Technology", "University");
	    prePostCategorize("Technical University", "University");
	    prePostCategorize("Memorial University", "University");
	    prePostCategorize("Academy of Medical Sciences", "University");
	    prePostCategorize("University of Agricultural Sciences", "University");
	    prePostCategorize("Academy", "University");
	    prePostCategorize("Chinese University", "University");
	    prePostCategorize("Charité-Universitätsmedizin", "University");
	    prePostCategorize("University of Medical Sciences", "University");
	    prePostCategorize("Academy of Agricultural Sciences", "University");
	    prePostCategorize("Universiteit", "University");
	    prePostCategorize("Universitatea", "University");
	    prePostCategorize("Universitetet", "University");
	    prePostCategorize("Univ", "University");
	    prePostCategorize("Universitar", "University");
	    prePostCategorize("l'Université", "University");
	    prePostCategorize("Universitat", "University");
	    prePostCategorize("Univ.", "University");
	    prePostCategorize("Universitäre", "University");
	    prePostCategorize("Universität-Gesamthochschule", "University");
	    prePostCategorize("Universitair", "University");
	    prePostCategorize("L'Université", "University");
	    prePostCategorize("Universitet", "University");

	    prePostCategorize("U.S. Department", "Agency");
	    prePostCategorize("United States Department", "Agency");
	    prePostCategorize("Ministry", "Agency");
	    prePostCategorize("USDA", "Agency");
	    prePostCategorize("National Center", "Agency");
	    prePostCategorize("National Centers", "Agency");
	    prePostCategorize("National Institute", "Agency");
	    prePostCategorize("National Institutes", "Agency");
	    prePostCategorize("Administration", "Agency");
	    prePostCategorize("Agency", "Agency");

	    prePostCategorize("Corporation", "Company");
	    prePostCategorize("Corp", "Company");
	    prePostCategorize("Corp.", "Company");
	    prePostCategorize("Incorporated", "Company");
	    prePostCategorize("Inc.", "Company");
	    prePostCategorize("Inc", "Company");
	    prePostCategorize("Limited", "Company");
	    prePostCategorize("Ltd", "Company");
	    prePostCategorize("Ltd.", "Company");
	    prePostCategorize("Company", "Company");

	    prePostCategorize("Foundation", "Foundation");
	    prePostCategorize("Fund", "Foundation");

	    prePostCategorize("Association", "Association");
	    prePostCategorize("Society", "Association");
	    prePostCategorize("Council", "Association");
	    prePostCategorize("Board", "Association");
	    prePostCategorize("Commission", "Association");
	}
	
	void infixCategorize() {
	    infixCategorize("University", "University");
	    infixCategorize("Universität", "University");
	    infixCategorize("Universidad", "University");
	    infixCategorize("Universitatea", "University");
	    infixCategorize("Universitätsmedizin", "University");
	    infixCategorize("Universitaires", "University");
	    infixCategorize("Università", "University");
	    infixCategorize("Universidade", "University");
	    infixCategorize("Universitet", "University");
	    infixCategorize("Univ.", "University");
	    infixCategorize("Univerziteta", "University");
	    infixCategorize("Universitario", "University");
	    infixCategorize("Universitäit", "University");
	    infixCategorize("Université", "University");
	    infixCategorize("dell'Università", "University");
	    infixCategorize("Univerzity", "University");
	    infixCategorize("Univerziteta", "University");
	    infixCategorize("Univerziteta", "University");
	    infixCategorize("Universiteit", "University");
	    
	    infixCategorize("Institute", "Institute");
	    infixCategorize("Institut", "Institute");
	    infixCategorize("Center", "Institute");
	    infixCategorize("Centre", "Institute");
	    
	    infixCategorize("Laboratory", "Laboratory");
	    
	    infixCategorize("Division", "Division");
	    
	    infixCategorize("Department", "Department");
	    
	    infixCategorize("College", "College");
	    infixCategorize("School", "College");
	    
	    infixCategorize("Hospital", "Hospital");
	    infixCategorize("Universitetshospital", "Hospital");
	    infixCategorize("Universitätskrankenhaus", "Hospital");
	    infixCategorize("Universitätsmedizin", "Hospital");
	    infixCategorize("Universitätsklinik", "Hospital");
	    infixCategorize("Universitäts-Krankenhaus", "Hospital");
	    infixCategorize("Universitätskliniken", "Hospital");
	    infixCategorize("Universitätskrankenhauses", "Hospital");
	    infixCategorize("Universitätsfrauenklinik", "Hospital");
	    infixCategorize("Universitäts-Frauenklinik", "Hospital");
	    infixCategorize("Universitäts-Augenklinik", "Hospital");
	    infixCategorize("Universitätsaugenklinik", "Hospital");
	    infixCategorize("Universitätskinderklinik", "Hospital");
	    infixCategorize("Universitäts-Kinderklinik", "Hospital");
	    infixCategorize("Universitetssjukhuset", "Hospital");
	    infixCategorize("UniversitätsSpital", "Hospital");
	    infixCategorize("Universitäts-Hautklinik", "Hospital");
	    infixCategorize("Universitäts-Kinderspital", "Hospital");
	    infixCategorize("Universitäts-HNO-Klinik", "Hospital");
	    infixCategorize("Universitäts-Hals-Nasen-Ohrenklinik", "Hospital");
	    infixCategorize("Universitätsspitals", "Hospital");
	    infixCategorize("Universität-Augenklinik", "Hospital");
	    infixCategorize("Universitäts Augenklinik", "Hospital");
	    infixCategorize("Univers.-Frauenklinik", "Hospital");
	    infixCategorize("Univ.-Klinik", "Hospital");
	    infixCategorize("Univ.-Hautklinik", "Hospital");
	    infixCategorize("Univ.-Augenklinik", "Hospital");
	    infixCategorize("Universitäts-Hals-Nasen-Ohren-Klinik", "Hospital");
	    infixCategorize("Univ.-HNO-Klinik", "Hospital");
	    infixCategorize("Univers.-Klinik", "Hospital");
	    infixCategorize("Universitäts-Hals-Nasen-Ohren-Klinik", "Hospital");
	}
	 
    void fullCategorize(String pattern, String category) {
        if (this.category == null && label.equals(pattern))
    	this.category = category;
    }
    
    void prePostCategorize(String pattern, String category) {
        if (this.category != null)
    	return;
        if (label.startsWith(pattern+" ") || label.endsWith(" "+pattern))
    	this.category = category;
    }
    
    void infixCategorize(String pattern, String category) {
        if (this.category != null)
    	return;
        if (label.contains(" "+pattern+" ") || label.contains("-"+pattern+" "))
    	this.category = category;
    }
}