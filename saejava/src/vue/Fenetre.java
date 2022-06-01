package vue;

import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.PI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import metier.Arete;
import metier.Graphe;
import metier.Sommet;
import modele.ModeleListAretes;
import modele.ModeleListSommets;

/**
 *
 * @author François, Elliot
 */
public class Fenetre extends javax.swing.JFrame {

		
	public Graphe graphePrincipal;
	private ModeleListSommets modeleVilles = new ModeleListSommets();
	private ModeleListSommets modeleRestaurants = new ModeleListSommets();
	private ModeleListSommets modeleLoisirs = new ModeleListSommets();
	private ModeleListAretes modeleAutoroutes = new ModeleListAretes();
	private ModeleListAretes modeleNationales = new ModeleListAretes();
	private ModeleListAretes modeleDepartementales = new ModeleListAretes();
	
	
	// Selection pour combo box avec tous les sommets OU toutes les arêtes (quelque soit le type)
	private DefaultComboBoxModel modeleEcran1SommetsCombo = new DefaultComboBoxModel();
	private DefaultComboBoxModel modeleEcran1AretesCombo = new DefaultComboBoxModel();
	private DefaultComboBoxModel modeleEcran2SommetsChoix1Combo = new DefaultComboBoxModel();
	private DefaultComboBoxModel modeleEcran2SommetsChoix2Combo = new DefaultComboBoxModel();

	
	// Resultat dans l'écran 1 de la recherche de voisins
	private DefaultListModel  modeleEcran1Sommets = new DefaultListModel();
	private DefaultListModel modeleEcran1Aretes = new DefaultListModel();
	//Resultat dans l'ecran 3 de la combo box
	private DefaultComboBoxModel modeleEcran3Choix1Combo = new DefaultComboBoxModel();
	private DefaultComboBoxModel modeleEcran3Choix2Combo = new DefaultComboBoxModel();
	
	
	public Fenetre() {
		initComponents();
	}
	
	public void importer() {
		if (jButtonAccueilImporter.getText().equals("Importer un graphe")) {
			JFileChooser choixFichier = new JFileChooser();
			int option = choixFichier.showOpenDialog(null);
			if(option == JFileChooser.APPROVE_OPTION){
				try {
					File file = choixFichier.getSelectedFile();
					graphePrincipal = new Graphe(file.getName());
					jTabbedPanePrincipal.setEnabled(true);

					int nbSommets = graphePrincipal.comptageSommets().get(0);
					int nbVilles = graphePrincipal.comptageSommets().get(1);
					int nbRestaurants = graphePrincipal.comptageSommets().get(2);
					int nbLoisir = graphePrincipal.comptageSommets().get(3);

					jLabelEcran0Villes.setText(nbVilles+" villes");
					jLabelEcran0Restaurants.setText(nbRestaurants+" restaurants");
					jLabelEcran0Loisirs.setText(nbLoisir+" lieux de loisir");

					int nbAretes = graphePrincipal.comptageAretes().get(0);
					int nbAutoroutes = graphePrincipal.comptageAretes().get(1);
					int nbNationales = graphePrincipal.comptageAretes().get(2);
					int nbDepartmentales = graphePrincipal.comptageAretes().get(3);

					jLabelEcran0Autoroutes.setText(nbAutoroutes+" Autoroutes");
					jLabelEcran0Nationales.setText(nbNationales+" Nationales");
					jLabelEcran0Departementales.setText(nbDepartmentales+" Departementales");

					jLabelEcran0Titre.setText("Informations sur le graphe : " + nbSommets + " sommets et " + nbAretes + " arêtes");

					modeleVilles.ajouterSommets(graphePrincipal.trouverSommetsParType("V"));
					modeleRestaurants.ajouterSommets(graphePrincipal.trouverSommetsParType("R"));
					modeleLoisirs.ajouterSommets(graphePrincipal.trouverSommetsParType("L"));

					modeleAutoroutes.ajouterAretes(graphePrincipal.trouverAretesParType("A"));
					modeleNationales.ajouterAretes(graphePrincipal.trouverAretesParType("N"));
					modeleDepartementales.ajouterAretes(graphePrincipal.trouverAretesParType("D"));
					
					for(Sommet elem :graphePrincipal.getListeSommet()){
						modeleEcran1SommetsCombo.addElement(elem);
						modeleEcran2SommetsChoix1Combo.addElement(elem);
						modeleEcran2SommetsChoix2Combo.addElement(elem);
					}
					
					for(Sommet elem:graphePrincipal.getListeSommet()){
						for(Arete arete:elem.getSuccesseurs()){
							modeleEcran1AretesCombo.addElement(arete);
						}	
					}
					
					for(Sommet elem : graphePrincipal.getListeSommet()){
						modeleEcran3Choix1Combo.addElement(elem);
					}
					
					for(Sommet elem :graphePrincipal.getListeSommet()){
						modeleEcran3Choix2Combo.addElement(elem);
					}
					
					int centerX = 400;    
					int centerY = 225; 
					int r = 200;
					Graphics2D drawFx = (Graphics2D)jPanelAccueilCentreHaut.getGraphics();
					List<Sommet> graphe = graphePrincipal.getListeSommet();

					drawFx.drawOval(centerX-200, centerY-200, 400, 400);
					for(int i = 0; i < graphe.size(); i++) {
						double angle = (PI * 2 / graphe.size() * i);
						int posX = (int) (r * Math.cos(angle));
						int posY = (int) (r * Math.sin(angle));
						System.out.println(angle + " " + posX + " " + posY);
						JLabel sommetEnCours = new JLabel();
						jPanelAccueilCentreHaut.add(sommetEnCours);
						sommetEnCours.setText(graphe.get(i).getNom());
						sommetEnCours.setFont(new Font("Helvetica Neue", Font.PLAIN, 10));
						sommetEnCours.setVisible(true);
						sommetEnCours.setLocation(centerX + posX - 50, centerY + posY - 15);
						sommetEnCours.setSize(100, 30);
						sommetEnCours.setHorizontalAlignment(JLabel.CENTER);
					}
					jButtonAccueilImporter.setText("Supprimer le graphe");
					jMenuItemFichierImporter.setEnabled(false);
				} catch (IOException e) {
					System.out.println("Erreur dans l'importation : " + e.getMessage());
				}
			}
		} else {
			jTabbedPanePrincipal.setEnabled(false);
			modeleVilles.viderModele();
			modeleLoisirs.viderModele();
			modeleRestaurants.viderModele();
			modeleAutoroutes.viderModele();
			modeleDepartementales.viderModele();
			modeleNationales.viderModele();
			jButtonAccueilImporter.setText("Importer un graphe");
			jMenuItemFichierImporter.setEnabled(true);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPanePrincipal = new javax.swing.JTabbedPane();
        jPanelAccueil = new javax.swing.JPanel();
        jPanelAccueilOuest = new javax.swing.JPanel();
        jPanelAccueilEst = new javax.swing.JPanel();
        jPanelAccueilSud = new javax.swing.JPanel();
        jPanelAccueilCentre = new javax.swing.JPanel();
        jPanelAccueilCentreHaut = new javax.swing.JPanel();
        jPanelAccueilCentreBas = new javax.swing.JPanel();
        jPanelAccueilCentreBasEst = new javax.swing.JPanel();
        jPanelAccueilCentreBasOuest = new javax.swing.JPanel();
        jPanelAccueilCentreBasNord = new javax.swing.JPanel();
        jPanelAccueilCentreBasSud = new javax.swing.JPanel();
        jButtonAccueilImporter = new javax.swing.JButton();
        jPanelEcran0 = new javax.swing.JPanel();
        jPanelEcran0Ouest = new javax.swing.JPanel();
        jPanelEcran0Est = new javax.swing.JPanel();
        jPanelEcran0Sud = new javax.swing.JPanel();
        jPanelEcran0Centre = new javax.swing.JPanel();
        jPanelEcran0CentreHaut = new javax.swing.JPanel();
        jLabelEcran0Titre = new javax.swing.JLabel();
        jPanelEcran0CentreBas = new javax.swing.JPanel();
        jPanelEcran0Villes = new javax.swing.JPanel();
        jLabelEcran0Villes = new javax.swing.JLabel();
        jScrollPaneEcran0Villes = new javax.swing.JScrollPane();
        jListEcran0Villes = new javax.swing.JList<>();
        jPanelEcran0Loisirs = new javax.swing.JPanel();
        jLabelEcran0Loisirs = new javax.swing.JLabel();
        jScrollPaneEcran0Loisirs = new javax.swing.JScrollPane();
        jListEcran0Loisirs = new javax.swing.JList<>();
        jPanelEcran0Restaurants = new javax.swing.JPanel();
        jLabelEcran0Restaurants = new javax.swing.JLabel();
        jScrollPaneEcran0Restaurants = new javax.swing.JScrollPane();
        jListEcran0Restaurants = new javax.swing.JList<>();
        jPanelEcran0Autoroutes = new javax.swing.JPanel();
        jLabelEcran0Autoroutes = new javax.swing.JLabel();
        jScrollPaneEcran0Autoroutes = new javax.swing.JScrollPane();
        jListEcran0Autoroutes = new javax.swing.JList<>();
        jPanelEcran0Nationales = new javax.swing.JPanel();
        jLabelEcran0Nationales = new javax.swing.JLabel();
        jScrollPaneEcran0Nationales = new javax.swing.JScrollPane();
        jListEcran0Nationales = new javax.swing.JList<>();
        jPanelEcran0Departementales = new javax.swing.JPanel();
        jLabelEcran0Departementales = new javax.swing.JLabel();
        jScrollPaneEcran0Departementales = new javax.swing.JScrollPane();
        jListEcran0Departementales = new javax.swing.JList<>();
        jPanelEcran1 = new javax.swing.JPanel();
        jPanelEcran1Est = new javax.swing.JPanel();
        jPanelEcran1Ouest = new javax.swing.JPanel();
        jPanelEcran1Sud = new javax.swing.JPanel();
        jPanelEcran1Centre = new javax.swing.JPanel();
        jPanelEcran1CentreGauche = new javax.swing.JPanel();
        jLabelEcran1GaucheTitre = new javax.swing.JLabel();
        jLabelEcran1GaucheChoix = new javax.swing.JLabel();
        jComboBoxEcran1GaucheChoix = new javax.swing.JComboBox<>();
        jLabelEcran1GaucheResultat = new javax.swing.JLabel();
        jScrollPaneEcran1GaucheResultat = new javax.swing.JScrollPane();
        jListEcran1GaucheResultat = new javax.swing.JList<>();
        jPanelEcran1GaucheEspacement = new javax.swing.JPanel();
        jPanelEcran1CentreDroite = new javax.swing.JPanel();
        jLabelEcran1DroiteTitre = new javax.swing.JLabel();
        jLabelEcran1DroiteChoix = new javax.swing.JLabel();
        jComboBoxEcran1DroiteChoix = new javax.swing.JComboBox<>();
        jLabelEcran1DroiteResultat = new javax.swing.JLabel();
        jScrollPaneEcran1DroiteResultat = new javax.swing.JScrollPane();
        jListEcran1DroiteResultat = new javax.swing.JList<>();
        jPanelEcran1DroiteEspacement = new javax.swing.JPanel();
        jPanelEcran2 = new javax.swing.JPanel();
        jPanelEcran2Est = new javax.swing.JPanel();
        jPanelEcran2Ouest = new javax.swing.JPanel();
        jPanelEcran2Sud = new javax.swing.JPanel();
        jPanelEcran2Centre = new javax.swing.JPanel();
        jLabelEcran2Titre = new javax.swing.JLabel();
        jLabelEcran2Choix1 = new javax.swing.JLabel();
        jComboBoxEcran2Choix1 = new javax.swing.JComboBox<>();
        jLabelEcran2Choix2 = new javax.swing.JLabel();
        jComboBoxEcran2Choix2 = new javax.swing.JComboBox<>();
        jButtonEcran2Valider = new javax.swing.JButton();
        jLabelEcran2InfoResultat = new javax.swing.JLabel();
        jLabelEcran2Resultat = new javax.swing.JLabel();
        jPanelEcran2Espacement = new javax.swing.JPanel();
        jPanelEcran3 = new javax.swing.JPanel();
        jPanelEcran3Est = new javax.swing.JPanel();
        jPanelEcran3Ouest = new javax.swing.JPanel();
        jPanelEcran3Sud = new javax.swing.JPanel();
        jPanelEcran3Centre = new javax.swing.JPanel();
        jLabelEcran3Titre = new javax.swing.JLabel();
        jLabelEcran3Choix1 = new javax.swing.JLabel();
        jComboBoxEcran3Choix1 = new javax.swing.JComboBox<>();
        jLabelEcran3Choix2 = new javax.swing.JLabel();
        jComboBoxEcran3Choix2 = new javax.swing.JComboBox<>();
        jButtonEcran3Valider = new javax.swing.JButton();
        jLabelEcran3InfoResultat = new javax.swing.JLabel();
        jLabelEcran3ResultatOuverture = new javax.swing.JLabel();
        jLabelEcran3ResultatGastronomie = new javax.swing.JLabel();
        jLabelEcran3ResultatCulture = new javax.swing.JLabel();
        jPanelEcran3Espacement = new javax.swing.JPanel();
        jMenuBarPrincipale = new javax.swing.JMenuBar();
        jMenuFichier = new javax.swing.JMenu();
        jMenuItemFichierImporter = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 600));

        jTabbedPanePrincipal.setEnabled(false);

        jPanelAccueil.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanelAccueilOuestLayout = new javax.swing.GroupLayout(jPanelAccueilOuest);
        jPanelAccueilOuest.setLayout(jPanelAccueilOuestLayout);
        jPanelAccueilOuestLayout.setHorizontalGroup(
            jPanelAccueilOuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelAccueilOuestLayout.setVerticalGroup(
            jPanelAccueilOuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanelAccueil.add(jPanelAccueilOuest, java.awt.BorderLayout.LINE_START);

        javax.swing.GroupLayout jPanelAccueilEstLayout = new javax.swing.GroupLayout(jPanelAccueilEst);
        jPanelAccueilEst.setLayout(jPanelAccueilEstLayout);
        jPanelAccueilEstLayout.setHorizontalGroup(
            jPanelAccueilEstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelAccueilEstLayout.setVerticalGroup(
            jPanelAccueilEstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanelAccueil.add(jPanelAccueilEst, java.awt.BorderLayout.LINE_END);

        jPanelAccueilSud.setLayout(new java.awt.BorderLayout());
        jPanelAccueil.add(jPanelAccueilSud, java.awt.BorderLayout.PAGE_END);

        jPanelAccueilCentre.setLayout(new javax.swing.BoxLayout(jPanelAccueilCentre, javax.swing.BoxLayout.PAGE_AXIS));

        jPanelAccueilCentreHaut.setMinimumSize(new java.awt.Dimension(0, 450));
        jPanelAccueilCentreHaut.setPreferredSize(new java.awt.Dimension(800, 450));

        javax.swing.GroupLayout jPanelAccueilCentreHautLayout = new javax.swing.GroupLayout(jPanelAccueilCentreHaut);
        jPanelAccueilCentreHaut.setLayout(jPanelAccueilCentreHautLayout);
        jPanelAccueilCentreHautLayout.setHorizontalGroup(
            jPanelAccueilCentreHautLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelAccueilCentreHautLayout.setVerticalGroup(
            jPanelAccueilCentreHautLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        jPanelAccueilCentre.add(jPanelAccueilCentreHaut);

        jPanelAccueilCentreBas.setMinimumSize(new java.awt.Dimension(278, 110));
        jPanelAccueilCentreBas.setPreferredSize(new java.awt.Dimension(800, 110));
        jPanelAccueilCentreBas.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanelAccueilCentreBasEstLayout = new javax.swing.GroupLayout(jPanelAccueilCentreBasEst);
        jPanelAccueilCentreBasEst.setLayout(jPanelAccueilCentreBasEstLayout);
        jPanelAccueilCentreBasEstLayout.setHorizontalGroup(
            jPanelAccueilCentreBasEstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelAccueilCentreBasEstLayout.setVerticalGroup(
            jPanelAccueilCentreBasEstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanelAccueilCentreBas.add(jPanelAccueilCentreBasEst, java.awt.BorderLayout.LINE_END);

        javax.swing.GroupLayout jPanelAccueilCentreBasOuestLayout = new javax.swing.GroupLayout(jPanelAccueilCentreBasOuest);
        jPanelAccueilCentreBasOuest.setLayout(jPanelAccueilCentreBasOuestLayout);
        jPanelAccueilCentreBasOuestLayout.setHorizontalGroup(
            jPanelAccueilCentreBasOuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelAccueilCentreBasOuestLayout.setVerticalGroup(
            jPanelAccueilCentreBasOuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanelAccueilCentreBas.add(jPanelAccueilCentreBasOuest, java.awt.BorderLayout.LINE_START);

        jPanelAccueilCentreBasNord.setPreferredSize(new java.awt.Dimension(800, 40));

        javax.swing.GroupLayout jPanelAccueilCentreBasNordLayout = new javax.swing.GroupLayout(jPanelAccueilCentreBasNord);
        jPanelAccueilCentreBasNord.setLayout(jPanelAccueilCentreBasNordLayout);
        jPanelAccueilCentreBasNordLayout.setHorizontalGroup(
            jPanelAccueilCentreBasNordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanelAccueilCentreBasNordLayout.setVerticalGroup(
            jPanelAccueilCentreBasNordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanelAccueilCentreBas.add(jPanelAccueilCentreBasNord, java.awt.BorderLayout.PAGE_START);

        jPanelAccueilCentreBasSud.setPreferredSize(new java.awt.Dimension(800, 40));
        jPanelAccueilCentreBasSud.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanelAccueilCentreBasSudLayout = new javax.swing.GroupLayout(jPanelAccueilCentreBasSud);
        jPanelAccueilCentreBasSud.setLayout(jPanelAccueilCentreBasSudLayout);
        jPanelAccueilCentreBasSudLayout.setHorizontalGroup(
            jPanelAccueilCentreBasSudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanelAccueilCentreBasSudLayout.setVerticalGroup(
            jPanelAccueilCentreBasSudLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanelAccueilCentreBas.add(jPanelAccueilCentreBasSud, java.awt.BorderLayout.PAGE_END);

        jButtonAccueilImporter.setText("Importer un graphe");
        jButtonAccueilImporter.setMaximumSize(new java.awt.Dimension(78, 20));
        jButtonAccueilImporter.setMinimumSize(new java.awt.Dimension(78, 20));
        jButtonAccueilImporter.setPreferredSize(new java.awt.Dimension(78, 20));
        jButtonAccueilImporter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAccueilImporterActionPerformed(evt);
            }
        });
        jPanelAccueilCentreBas.add(jButtonAccueilImporter, java.awt.BorderLayout.CENTER);

        jPanelAccueilCentre.add(jPanelAccueilCentreBas);

        jPanelAccueil.add(jPanelAccueilCentre, java.awt.BorderLayout.CENTER);

        jTabbedPanePrincipal.addTab("Accueil", jPanelAccueil);

        jPanelEcran0.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanelEcran0OuestLayout = new javax.swing.GroupLayout(jPanelEcran0Ouest);
        jPanelEcran0Ouest.setLayout(jPanelEcran0OuestLayout);
        jPanelEcran0OuestLayout.setHorizontalGroup(
            jPanelEcran0OuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelEcran0OuestLayout.setVerticalGroup(
            jPanelEcran0OuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanelEcran0.add(jPanelEcran0Ouest, java.awt.BorderLayout.LINE_START);

        javax.swing.GroupLayout jPanelEcran0EstLayout = new javax.swing.GroupLayout(jPanelEcran0Est);
        jPanelEcran0Est.setLayout(jPanelEcran0EstLayout);
        jPanelEcran0EstLayout.setHorizontalGroup(
            jPanelEcran0EstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelEcran0EstLayout.setVerticalGroup(
            jPanelEcran0EstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanelEcran0.add(jPanelEcran0Est, java.awt.BorderLayout.LINE_END);

        jPanelEcran0Sud.setLayout(new java.awt.BorderLayout());
        jPanelEcran0.add(jPanelEcran0Sud, java.awt.BorderLayout.PAGE_END);

        jPanelEcran0Centre.setLayout(new javax.swing.BoxLayout(jPanelEcran0Centre, javax.swing.BoxLayout.PAGE_AXIS));

        jPanelEcran0CentreHaut.setPreferredSize(new java.awt.Dimension(800, 50));
        jPanelEcran0CentreHaut.setLayout(new java.awt.BorderLayout());

        jLabelEcran0Titre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEcran0Titre.setText("Informations sur le graphe");
        jPanelEcran0CentreHaut.add(jLabelEcran0Titre, java.awt.BorderLayout.CENTER);

        jPanelEcran0Centre.add(jPanelEcran0CentreHaut);

        jPanelEcran0CentreBas.setPreferredSize(new java.awt.Dimension(800, 600));
        jPanelEcran0CentreBas.setLayout(new java.awt.GridLayout(2, 3, 20, 20));

        jPanelEcran0Villes.setLayout(new java.awt.BorderLayout());

        jLabelEcran0Villes.setText("Villes");
        jPanelEcran0Villes.add(jLabelEcran0Villes, java.awt.BorderLayout.PAGE_END);

        jListEcran0Villes.setModel(modeleVilles);
        jScrollPaneEcran0Villes.setViewportView(jListEcran0Villes);

        jPanelEcran0Villes.add(jScrollPaneEcran0Villes, java.awt.BorderLayout.CENTER);

        jPanelEcran0CentreBas.add(jPanelEcran0Villes);

        jPanelEcran0Loisirs.setLayout(new java.awt.BorderLayout());

        jLabelEcran0Loisirs.setText("Lieux de loisir");
        jPanelEcran0Loisirs.add(jLabelEcran0Loisirs, java.awt.BorderLayout.PAGE_END);

        jListEcran0Loisirs.setModel(modeleLoisirs);
        jScrollPaneEcran0Loisirs.setViewportView(jListEcran0Loisirs);

        jPanelEcran0Loisirs.add(jScrollPaneEcran0Loisirs, java.awt.BorderLayout.CENTER);

        jPanelEcran0CentreBas.add(jPanelEcran0Loisirs);

        jPanelEcran0Restaurants.setLayout(new java.awt.BorderLayout());

        jLabelEcran0Restaurants.setText("Restaurants");
        jPanelEcran0Restaurants.add(jLabelEcran0Restaurants, java.awt.BorderLayout.PAGE_END);

        jListEcran0Restaurants.setModel(modeleRestaurants);
        jScrollPaneEcran0Restaurants.setViewportView(jListEcran0Restaurants);

        jPanelEcran0Restaurants.add(jScrollPaneEcran0Restaurants, java.awt.BorderLayout.CENTER);

        jPanelEcran0CentreBas.add(jPanelEcran0Restaurants);

        jPanelEcran0Autoroutes.setLayout(new java.awt.BorderLayout());

        jLabelEcran0Autoroutes.setText("Autoroutes");
        jPanelEcran0Autoroutes.add(jLabelEcran0Autoroutes, java.awt.BorderLayout.PAGE_END);

        jListEcran0Autoroutes.setModel(modeleAutoroutes);
        jScrollPaneEcran0Autoroutes.setViewportView(jListEcran0Autoroutes);

        jPanelEcran0Autoroutes.add(jScrollPaneEcran0Autoroutes, java.awt.BorderLayout.CENTER);

        jPanelEcran0CentreBas.add(jPanelEcran0Autoroutes);

        jPanelEcran0Nationales.setLayout(new java.awt.BorderLayout());

        jLabelEcran0Nationales.setText("Nationales");
        jPanelEcran0Nationales.add(jLabelEcran0Nationales, java.awt.BorderLayout.PAGE_END);

        jListEcran0Nationales.setModel(modeleNationales);
        jScrollPaneEcran0Nationales.setViewportView(jListEcran0Nationales);

        jPanelEcran0Nationales.add(jScrollPaneEcran0Nationales, java.awt.BorderLayout.CENTER);

        jPanelEcran0CentreBas.add(jPanelEcran0Nationales);

        jPanelEcran0Departementales.setLayout(new java.awt.BorderLayout());

        jLabelEcran0Departementales.setText("Departementales");
        jPanelEcran0Departementales.add(jLabelEcran0Departementales, java.awt.BorderLayout.PAGE_END);

        jListEcran0Departementales.setModel(modeleDepartementales);
        jScrollPaneEcran0Departementales.setViewportView(jListEcran0Departementales);

        jPanelEcran0Departementales.add(jScrollPaneEcran0Departementales, java.awt.BorderLayout.CENTER);

        jPanelEcran0CentreBas.add(jPanelEcran0Departementales);

        jPanelEcran0Centre.add(jPanelEcran0CentreBas);

        jPanelEcran0.add(jPanelEcran0Centre, java.awt.BorderLayout.CENTER);

        jTabbedPanePrincipal.addTab("Informations", jPanelEcran0);

        jPanelEcran1.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanelEcran1EstLayout = new javax.swing.GroupLayout(jPanelEcran1Est);
        jPanelEcran1Est.setLayout(jPanelEcran1EstLayout);
        jPanelEcran1EstLayout.setHorizontalGroup(
            jPanelEcran1EstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelEcran1EstLayout.setVerticalGroup(
            jPanelEcran1EstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanelEcran1.add(jPanelEcran1Est, java.awt.BorderLayout.LINE_END);

        javax.swing.GroupLayout jPanelEcran1OuestLayout = new javax.swing.GroupLayout(jPanelEcran1Ouest);
        jPanelEcran1Ouest.setLayout(jPanelEcran1OuestLayout);
        jPanelEcran1OuestLayout.setHorizontalGroup(
            jPanelEcran1OuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelEcran1OuestLayout.setVerticalGroup(
            jPanelEcran1OuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanelEcran1.add(jPanelEcran1Ouest, java.awt.BorderLayout.LINE_START);

        jPanelEcran1Sud.setLayout(new java.awt.BorderLayout());
        jPanelEcran1.add(jPanelEcran1Sud, java.awt.BorderLayout.PAGE_END);

        jPanelEcran1Centre.setLayout(new java.awt.GridLayout(1, 1, 20, 0));

        java.awt.GridBagLayout jPanel31Layout = new java.awt.GridBagLayout();
        jPanel31Layout.rowHeights = new int[] {100};
        jPanelEcran1CentreGauche.setLayout(jPanel31Layout);

        jLabelEcran1GaucheTitre.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabelEcran1GaucheTitre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEcran1GaucheTitre.setText("Voisins directs d'un sommet");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 5.0;
        jPanelEcran1CentreGauche.add(jLabelEcran1GaucheTitre, gridBagConstraints);

        jLabelEcran1GaucheChoix.setText("Choisir le sommet :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran1CentreGauche.add(jLabelEcran1GaucheChoix, gridBagConstraints);

        jComboBoxEcran1GaucheChoix.setModel(modeleEcran1SommetsCombo);
        jComboBoxEcran1GaucheChoix.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxEcran1GaucheChoixItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran1CentreGauche.add(jComboBoxEcran1GaucheChoix, gridBagConstraints);

        jLabelEcran1GaucheResultat.setText("Voisins directs :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran1CentreGauche.add(jLabelEcran1GaucheResultat, gridBagConstraints);

        jListEcran1GaucheResultat.setModel(modeleEcran1Sommets);
        jScrollPaneEcran1GaucheResultat.setViewportView(jListEcran1GaucheResultat);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran1CentreGauche.add(jScrollPaneEcran1GaucheResultat, gridBagConstraints);

        javax.swing.GroupLayout jPanelEcran1GaucheEspacementLayout = new javax.swing.GroupLayout(jPanelEcran1GaucheEspacement);
        jPanelEcran1GaucheEspacement.setLayout(jPanelEcran1GaucheEspacementLayout);
        jPanelEcran1GaucheEspacementLayout.setHorizontalGroup(
            jPanelEcran1GaucheEspacementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelEcran1GaucheEspacementLayout.setVerticalGroup(
            jPanelEcran1GaucheEspacementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.weighty = 3.0;
        jPanelEcran1CentreGauche.add(jPanelEcran1GaucheEspacement, gridBagConstraints);

        jPanelEcran1Centre.add(jPanelEcran1CentreGauche);

        jPanelEcran1CentreDroite.setLayout(new java.awt.GridBagLayout());

        jLabelEcran1DroiteTitre.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        jLabelEcran1DroiteTitre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEcran1DroiteTitre.setText("Sommets reliés par une arête");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 70;
        gridBagConstraints.weighty = 5.0;
        jPanelEcran1CentreDroite.add(jLabelEcran1DroiteTitre, gridBagConstraints);

        jLabelEcran1DroiteChoix.setText("Choisir l'arête :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran1CentreDroite.add(jLabelEcran1DroiteChoix, gridBagConstraints);

        jComboBoxEcran1DroiteChoix.setModel(modeleEcran1AretesCombo);
        jComboBoxEcran1DroiteChoix.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxEcran1DroiteChoixItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran1CentreDroite.add(jComboBoxEcran1DroiteChoix, gridBagConstraints);

        jLabelEcran1DroiteResultat.setText("Sommets reliés :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran1CentreDroite.add(jLabelEcran1DroiteResultat, gridBagConstraints);

        jListEcran1DroiteResultat.setModel(modeleEcran1Aretes);
        jScrollPaneEcran1DroiteResultat.setViewportView(jListEcran1DroiteResultat);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran1CentreDroite.add(jScrollPaneEcran1DroiteResultat, gridBagConstraints);

        jPanelEcran1DroiteEspacement.setMaximumSize(new java.awt.Dimension(32767, 0));

        javax.swing.GroupLayout jPanelEcran1DroiteEspacementLayout = new javax.swing.GroupLayout(jPanelEcran1DroiteEspacement);
        jPanelEcran1DroiteEspacement.setLayout(jPanelEcran1DroiteEspacementLayout);
        jPanelEcran1DroiteEspacementLayout.setHorizontalGroup(
            jPanelEcran1DroiteEspacementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelEcran1DroiteEspacementLayout.setVerticalGroup(
            jPanelEcran1DroiteEspacementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.weighty = 3.0;
        jPanelEcran1CentreDroite.add(jPanelEcran1DroiteEspacement, gridBagConstraints);

        jPanelEcran1Centre.add(jPanelEcran1CentreDroite);

        jPanelEcran1.add(jPanelEcran1Centre, java.awt.BorderLayout.CENTER);

        jTabbedPanePrincipal.addTab("1-distance", jPanelEcran1);

        jPanelEcran2.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanelEcran2EstLayout = new javax.swing.GroupLayout(jPanelEcran2Est);
        jPanelEcran2Est.setLayout(jPanelEcran2EstLayout);
        jPanelEcran2EstLayout.setHorizontalGroup(
            jPanelEcran2EstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelEcran2EstLayout.setVerticalGroup(
            jPanelEcran2EstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanelEcran2.add(jPanelEcran2Est, java.awt.BorderLayout.LINE_END);

        javax.swing.GroupLayout jPanelEcran2OuestLayout = new javax.swing.GroupLayout(jPanelEcran2Ouest);
        jPanelEcran2Ouest.setLayout(jPanelEcran2OuestLayout);
        jPanelEcran2OuestLayout.setHorizontalGroup(
            jPanelEcran2OuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelEcran2OuestLayout.setVerticalGroup(
            jPanelEcran2OuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanelEcran2.add(jPanelEcran2Ouest, java.awt.BorderLayout.LINE_START);

        jPanelEcran2Sud.setLayout(new java.awt.BorderLayout());
        jPanelEcran2.add(jPanelEcran2Sud, java.awt.BorderLayout.PAGE_END);

        jPanelEcran2Centre.setLayout(new java.awt.GridBagLayout());

        jLabelEcran2Titre.setFont(new java.awt.Font("Helvetica Neue", 0, 36)); // NOI18N
        jLabelEcran2Titre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEcran2Titre.setText("2-distance exacte entre deux sommets");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 5.0;
        jPanelEcran2Centre.add(jLabelEcran2Titre, gridBagConstraints);

        jLabelEcran2Choix1.setText("Choisir le premier sommet :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran2Centre.add(jLabelEcran2Choix1, gridBagConstraints);

        jComboBoxEcran2Choix1.setModel(modeleEcran2SommetsChoix1Combo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran2Centre.add(jComboBoxEcran2Choix1, gridBagConstraints);

        jLabelEcran2Choix2.setText("Choisir le deuxième sommet :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran2Centre.add(jLabelEcran2Choix2, gridBagConstraints);

        jComboBoxEcran2Choix2.setModel(modeleEcran2SommetsChoix2Combo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran2Centre.add(jComboBoxEcran2Choix2, gridBagConstraints);

        jButtonEcran2Valider.setText("Vérifier la 2-distance");
        jButtonEcran2Valider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEcran2ValiderActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran2Centre.add(jButtonEcran2Valider, gridBagConstraints);

        jLabelEcran2InfoResultat.setText("Résultat :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran2Centre.add(jLabelEcran2InfoResultat, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran2Centre.add(jLabelEcran2Resultat, gridBagConstraints);

        jPanelEcran2Espacement.setMaximumSize(new java.awt.Dimension(32767, 0));
        jPanelEcran2Espacement.setMinimumSize(new java.awt.Dimension(100, 0));
        jPanelEcran2Espacement.setPreferredSize(new java.awt.Dimension(100, 0));

        javax.swing.GroupLayout jPanelEcran2EspacementLayout = new javax.swing.GroupLayout(jPanelEcran2Espacement);
        jPanelEcran2Espacement.setLayout(jPanelEcran2EspacementLayout);
        jPanelEcran2EspacementLayout.setHorizontalGroup(
            jPanelEcran2EspacementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelEcran2EspacementLayout.setVerticalGroup(
            jPanelEcran2EspacementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.weighty = 3.0;
        jPanelEcran2Centre.add(jPanelEcran2Espacement, gridBagConstraints);

        jPanelEcran2.add(jPanelEcran2Centre, java.awt.BorderLayout.CENTER);

        jTabbedPanePrincipal.addTab("2-distance", jPanelEcran2);

        jPanelEcran3.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanelEcran3EstLayout = new javax.swing.GroupLayout(jPanelEcran3Est);
        jPanelEcran3Est.setLayout(jPanelEcran3EstLayout);
        jPanelEcran3EstLayout.setHorizontalGroup(
            jPanelEcran3EstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelEcran3EstLayout.setVerticalGroup(
            jPanelEcran3EstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanelEcran3.add(jPanelEcran3Est, java.awt.BorderLayout.LINE_END);

        javax.swing.GroupLayout jPanelEcran3OuestLayout = new javax.swing.GroupLayout(jPanelEcran3Ouest);
        jPanelEcran3Ouest.setLayout(jPanelEcran3OuestLayout);
        jPanelEcran3OuestLayout.setHorizontalGroup(
            jPanelEcran3OuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelEcran3OuestLayout.setVerticalGroup(
            jPanelEcran3OuestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jPanelEcran3.add(jPanelEcran3Ouest, java.awt.BorderLayout.LINE_START);

        jPanelEcran3Sud.setLayout(new java.awt.BorderLayout());
        jPanelEcran3.add(jPanelEcran3Sud, java.awt.BorderLayout.PAGE_END);

        jPanelEcran3Centre.setLayout(new java.awt.GridBagLayout());

        jLabelEcran3Titre.setFont(new java.awt.Font("Helvetica Neue", 0, 36)); // NOI18N
        jLabelEcran3Titre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEcran3Titre.setText("Comparer deux sommets");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 5.0;
        jPanelEcran3Centre.add(jLabelEcran3Titre, gridBagConstraints);

        jLabelEcran3Choix1.setText("Choisir le premier sommet :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran3Centre.add(jLabelEcran3Choix1, gridBagConstraints);

        jComboBoxEcran3Choix1.setModel(modeleEcran3Choix1Combo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran3Centre.add(jComboBoxEcran3Choix1, gridBagConstraints);

        jLabelEcran3Choix2.setText("Choisir le deuxième sommet :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran3Centre.add(jLabelEcran3Choix2, gridBagConstraints);

        jComboBoxEcran3Choix2.setModel(modeleEcran3Choix2Combo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran3Centre.add(jComboBoxEcran3Choix2, gridBagConstraints);

        jButtonEcran3Valider.setText("Comparer");
        jButtonEcran3Valider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEcran3ValiderActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran3Centre.add(jButtonEcran3Valider, gridBagConstraints);

        jLabelEcran3InfoResultat.setText("Résultat :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran3Centre.add(jLabelEcran3InfoResultat, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran3Centre.add(jLabelEcran3ResultatOuverture, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran3Centre.add(jLabelEcran3ResultatGastronomie, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        jPanelEcran3Centre.add(jLabelEcran3ResultatCulture, gridBagConstraints);

        jPanelEcran3Espacement.setMaximumSize(new java.awt.Dimension(32767, 0));
        jPanelEcran3Espacement.setMinimumSize(new java.awt.Dimension(100, 0));

        javax.swing.GroupLayout jPanelEcran3EspacementLayout = new javax.swing.GroupLayout(jPanelEcran3Espacement);
        jPanelEcran3Espacement.setLayout(jPanelEcran3EspacementLayout);
        jPanelEcran3EspacementLayout.setHorizontalGroup(
            jPanelEcran3EspacementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelEcran3EspacementLayout.setVerticalGroup(
            jPanelEcran3EspacementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.weighty = 3.0;
        jPanelEcran3Centre.add(jPanelEcran3Espacement, gridBagConstraints);

        jPanelEcran3.add(jPanelEcran3Centre, java.awt.BorderLayout.CENTER);

        jTabbedPanePrincipal.addTab("Comparaisons", jPanelEcran3);

        getContentPane().add(jTabbedPanePrincipal, java.awt.BorderLayout.CENTER);

        jMenuFichier.setText("Fichier");

        jMenuItemFichierImporter.setText("Importer");
        jMenuItemFichierImporter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFichierImporterActionPerformed(evt);
            }
        });
        jMenuFichier.add(jMenuItemFichierImporter);

        jMenuBarPrincipale.add(jMenuFichier);

        setJMenuBar(jMenuBarPrincipale);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAccueilImporterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAccueilImporterActionPerformed
		importer();
    }//GEN-LAST:event_jButtonAccueilImporterActionPerformed

    private void jComboBoxEcran1GaucheChoixItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxEcran1GaucheChoixItemStateChanged
        modeleEcran1Sommets.clear();
		for(Sommet s:graphePrincipal.getListeSommet()){
		   if(jComboBoxEcran1GaucheChoix.getSelectedItem()==s){
			   for(Arete elem:s.getSuccesseurs()){
					modeleEcran1Sommets.addElement(elem.getDestination());
				}
			}
		}
    }//GEN-LAST:event_jComboBoxEcran1GaucheChoixItemStateChanged

    private void jComboBoxEcran1DroiteChoixItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxEcran1DroiteChoixItemStateChanged
		Object val = jComboBoxEcran1DroiteChoix.getSelectedItem();
		modeleEcran1Aretes.clear();
		for(Sommet elem:graphePrincipal.trouverSommetsRelies((Arete) val)){
			modeleEcran1Aretes.addElement(elem);
		}
    }//GEN-LAST:event_jComboBoxEcran1DroiteChoixItemStateChanged

    private void jButtonEcran2ValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEcran2ValiderActionPerformed
        Sommet s1 = (Sommet) jComboBoxEcran2Choix1.getSelectedItem();
		Sommet s2 = (Sommet) jComboBoxEcran2Choix2.getSelectedItem();
		boolean resultat = graphePrincipal.rechercher2Distance(s1, s2);
		String completion;
		if (resultat) completion = "sont";
		else completion = "ne sont pas";
		jLabelEcran2Resultat.setText("Les sommets " + s1.getNom() + " et " + s2.getNom() + " " + completion + " situés à une 2-distance exactement l'un de l'autre");
    }//GEN-LAST:event_jButtonEcran2ValiderActionPerformed

    private void jButtonEcran3ValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEcran3ValiderActionPerformed
        List<Integer> resultat = new ArrayList<>();
		Sommet choix1 = (Sommet)jComboBoxEcran3Choix1.getSelectedItem();
		Sommet choix2 = (Sommet)jComboBoxEcran3Choix2.getSelectedItem();
		resultat = graphePrincipal.comparerOCG(choix1, choix2);
		int resultatOuverture = resultat.get(0);
		int resultatGastronomie = resultat.get(1);
		int resultatCulture = resultat.get(2);
		String comparaisonOuverture = "";
		String comparaisonGastronomie = "";
		String comparaisonCulture = "";
		switch (resultatOuverture) {
			case -1: comparaisonOuverture = "est moins";
				break;
			case 0: comparaisonOuverture = "est autant";
				break;
			case 1: comparaisonOuverture = "est plus";
				break;
		}
		switch (resultatGastronomie) {
			case -1: comparaisonGastronomie = "est moins";
				break;
			case 0: comparaisonGastronomie = "est autant";
				break;
			case 1: comparaisonGastronomie = "est plus";
				break;
		}
		switch (resultatCulture) {
			case -1: comparaisonCulture = "est moins";
				break;
			case 0: comparaisonCulture = "est autant";
				break;
			case 1: comparaisonCulture = "est plus";
				break;
		}
		jLabelEcran3ResultatOuverture.setText(choix1.getNom() + " " + comparaisonOuverture + " ouvert que " + choix2.getNom());
		jLabelEcran3ResultatGastronomie.setText(choix1.getNom() + " " + comparaisonGastronomie + " gastronomique que " + choix2.getNom());
		jLabelEcran3ResultatCulture.setText(choix1.getNom() + " " + comparaisonCulture + " culturel que " + choix2.getNom());
    }//GEN-LAST:event_jButtonEcran3ValiderActionPerformed

    private void jMenuItemFichierImporterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFichierImporterActionPerformed
        importer();
    }//GEN-LAST:event_jMenuItemFichierImporterActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Fenetre.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Fenetre().setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAccueilImporter;
    private javax.swing.JButton jButtonEcran2Valider;
    private javax.swing.JButton jButtonEcran3Valider;
    private javax.swing.JComboBox<String> jComboBoxEcran1DroiteChoix;
    private javax.swing.JComboBox<String> jComboBoxEcran1GaucheChoix;
    private javax.swing.JComboBox<String> jComboBoxEcran2Choix1;
    private javax.swing.JComboBox<String> jComboBoxEcran2Choix2;
    private javax.swing.JComboBox<String> jComboBoxEcran3Choix1;
    private javax.swing.JComboBox<String> jComboBoxEcran3Choix2;
    private javax.swing.JLabel jLabelEcran0Autoroutes;
    private javax.swing.JLabel jLabelEcran0Departementales;
    private javax.swing.JLabel jLabelEcran0Loisirs;
    private javax.swing.JLabel jLabelEcran0Nationales;
    private javax.swing.JLabel jLabelEcran0Restaurants;
    private javax.swing.JLabel jLabelEcran0Titre;
    private javax.swing.JLabel jLabelEcran0Villes;
    private javax.swing.JLabel jLabelEcran1DroiteChoix;
    private javax.swing.JLabel jLabelEcran1DroiteResultat;
    private javax.swing.JLabel jLabelEcran1DroiteTitre;
    private javax.swing.JLabel jLabelEcran1GaucheChoix;
    private javax.swing.JLabel jLabelEcran1GaucheResultat;
    private javax.swing.JLabel jLabelEcran1GaucheTitre;
    private javax.swing.JLabel jLabelEcran2Choix1;
    private javax.swing.JLabel jLabelEcran2Choix2;
    private javax.swing.JLabel jLabelEcran2InfoResultat;
    private javax.swing.JLabel jLabelEcran2Resultat;
    private javax.swing.JLabel jLabelEcran2Titre;
    private javax.swing.JLabel jLabelEcran3Choix1;
    private javax.swing.JLabel jLabelEcran3Choix2;
    private javax.swing.JLabel jLabelEcran3InfoResultat;
    private javax.swing.JLabel jLabelEcran3ResultatCulture;
    private javax.swing.JLabel jLabelEcran3ResultatGastronomie;
    private javax.swing.JLabel jLabelEcran3ResultatOuverture;
    private javax.swing.JLabel jLabelEcran3Titre;
    private javax.swing.JList<String> jListEcran0Autoroutes;
    private javax.swing.JList<String> jListEcran0Departementales;
    private javax.swing.JList<String> jListEcran0Loisirs;
    private javax.swing.JList<String> jListEcran0Nationales;
    private javax.swing.JList<String> jListEcran0Restaurants;
    private javax.swing.JList<String> jListEcran0Villes;
    private javax.swing.JList<String> jListEcran1DroiteResultat;
    private javax.swing.JList<String> jListEcran1GaucheResultat;
    private javax.swing.JMenuBar jMenuBarPrincipale;
    private javax.swing.JMenu jMenuFichier;
    private javax.swing.JMenuItem jMenuItemFichierImporter;
    private javax.swing.JPanel jPanelAccueil;
    private javax.swing.JPanel jPanelAccueilCentre;
    private javax.swing.JPanel jPanelAccueilCentreBas;
    private javax.swing.JPanel jPanelAccueilCentreBasEst;
    private javax.swing.JPanel jPanelAccueilCentreBasNord;
    private javax.swing.JPanel jPanelAccueilCentreBasOuest;
    private javax.swing.JPanel jPanelAccueilCentreBasSud;
    private javax.swing.JPanel jPanelAccueilCentreHaut;
    private javax.swing.JPanel jPanelAccueilEst;
    private javax.swing.JPanel jPanelAccueilOuest;
    private javax.swing.JPanel jPanelAccueilSud;
    private javax.swing.JPanel jPanelEcran0;
    private javax.swing.JPanel jPanelEcran0Autoroutes;
    private javax.swing.JPanel jPanelEcran0Centre;
    private javax.swing.JPanel jPanelEcran0CentreBas;
    private javax.swing.JPanel jPanelEcran0CentreHaut;
    private javax.swing.JPanel jPanelEcran0Departementales;
    private javax.swing.JPanel jPanelEcran0Est;
    private javax.swing.JPanel jPanelEcran0Loisirs;
    private javax.swing.JPanel jPanelEcran0Nationales;
    private javax.swing.JPanel jPanelEcran0Ouest;
    private javax.swing.JPanel jPanelEcran0Restaurants;
    private javax.swing.JPanel jPanelEcran0Sud;
    private javax.swing.JPanel jPanelEcran0Villes;
    private javax.swing.JPanel jPanelEcran1;
    private javax.swing.JPanel jPanelEcran1Centre;
    private javax.swing.JPanel jPanelEcran1CentreDroite;
    private javax.swing.JPanel jPanelEcran1CentreGauche;
    private javax.swing.JPanel jPanelEcran1DroiteEspacement;
    private javax.swing.JPanel jPanelEcran1Est;
    private javax.swing.JPanel jPanelEcran1GaucheEspacement;
    private javax.swing.JPanel jPanelEcran1Ouest;
    private javax.swing.JPanel jPanelEcran1Sud;
    private javax.swing.JPanel jPanelEcran2;
    private javax.swing.JPanel jPanelEcran2Centre;
    private javax.swing.JPanel jPanelEcran2Espacement;
    private javax.swing.JPanel jPanelEcran2Est;
    private javax.swing.JPanel jPanelEcran2Ouest;
    private javax.swing.JPanel jPanelEcran2Sud;
    private javax.swing.JPanel jPanelEcran3;
    private javax.swing.JPanel jPanelEcran3Centre;
    private javax.swing.JPanel jPanelEcran3Espacement;
    private javax.swing.JPanel jPanelEcran3Est;
    private javax.swing.JPanel jPanelEcran3Ouest;
    private javax.swing.JPanel jPanelEcran3Sud;
    private javax.swing.JScrollPane jScrollPaneEcran0Autoroutes;
    private javax.swing.JScrollPane jScrollPaneEcran0Departementales;
    private javax.swing.JScrollPane jScrollPaneEcran0Loisirs;
    private javax.swing.JScrollPane jScrollPaneEcran0Nationales;
    private javax.swing.JScrollPane jScrollPaneEcran0Restaurants;
    private javax.swing.JScrollPane jScrollPaneEcran0Villes;
    private javax.swing.JScrollPane jScrollPaneEcran1DroiteResultat;
    private javax.swing.JScrollPane jScrollPaneEcran1GaucheResultat;
    private javax.swing.JTabbedPane jTabbedPanePrincipal;
    // End of variables declaration//GEN-END:variables
}