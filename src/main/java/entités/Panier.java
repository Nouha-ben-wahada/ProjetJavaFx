package entités;

import java.util.HashMap;
import java.util.Map;

public class Panier {
    // Map pour stocker les produits et leurs quantités
    private Map<Produit, Integer> produits = new HashMap<>();

    // Ajouter un produit avec une quantité (par défaut 1 si non spécifiée)
    public void ajouterProduit(Produit produit) {
        produits.put(produit, produits.getOrDefault(produit, 0) + 1);
    }

    public void ajouterProduit(Produit produit, int quantite) {
        produits.put(produit, produits.getOrDefault(produit, 0) + quantite);
    }

    // Supprimer un produit complètement du panier
    public void supprimerProduit(Produit produit) {
        produits.remove(produit);
    }

    // Mettre à jour la quantité d'un produit
    public void mettreAJourQuantite(Produit produit, int nouvelleQuantite) {
        if (nouvelleQuantite <= 0) {
            produits.remove(produit); // Supprimer si quantité <= 0
        } else {
            produits.put(produit, nouvelleQuantite);
        }
    }

    // Obtenir la quantité d'un produit dans le panier
    public int getQuantite(Produit produit) {
        return produits.getOrDefault(produit, 0);
    }

    // Calculer le prix total du panier
    public double calculerTotal() {
        double total = 0;
        for (Map.Entry<Produit, Integer> entry : produits.entrySet()) {
            total += entry.getKey().getPrix() * entry.getValue();
        }
        return total;
    }

    // Récupérer tous les produits dans le panier
    public Map<Produit, Integer> getProduits() {
        return produits;
    }

    public void setProduits(Map<Produit, Integer> produits) {
        this.produits = produits;
    }

    // Vider le panier
    public void viderPanier() {
        produits.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Panier{");
        sb.append("produits=\n");
        for (Map.Entry<Produit, Integer> entry : produits.entrySet()) {
            sb.append(entry.getKey().toString())
                    .append(", quantité=")
                    .append(entry.getValue())
                    .append("\n");
        }
        sb.append('}');
        return sb.toString();
    }
}
