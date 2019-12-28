
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Queue;
import java.util.Set;

import interfaces.ITree;

/**
 * Un arbre de recherche binaire (BST),
 * que l'on peut aussi parfois appeler arbre binaire ordonné ou trié,
 * est une structure de données en arbre binaire basée sur des nœuds
 * qui a les propriétés suivantes :
 * 1) Le sous-arbre gauche d'un nœud ne contient que les nœuds dont
 * la clé est inférieure à celle du nœud.
 * 2) Le sous-arbre de droite d'un nœud ne contient que les nœuds dont
 * les clés sont supérieures à celle du nœud.
 * 3) Les sous-arbres de gauche et de droite doivent
 * également être des arbres de recherche binaires.
 *
 * Traduit avec www.DeepL.com/Translator (version gratuite)
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
@SuppressWarnings("unchecked")
public class BinarySearchTree<T extends Comparable<T>> implements ITree<T> {

    private int modifications = 0;

    protected static final Random RANDOM = new Random();

    protected Node<T> root = null;
    protected int size = 0;
    protected INodeCreator<T> creator = null;

    public enum DepthFirstSearchOrder {
        inOrder, preOrder, postOrder
    }

    /**
     * Default constructor.
     */
    public BinarySearchTree() {
        this.creator = new INodeCreator<T>() {
            /**
             * {@inheritDoc}
             */
            @Override
            public Node<T> createNewNode(Node<T> parent, T id) {
                return (new Node<T>(parent, id));
            }
        };
    }

    /**
     * Constructor with external Node creator.
     */
    public BinarySearchTree(INodeCreator<T> creator) {
        this.creator = creator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(T value) {
        Node<T> nodeAdded = this.addValue(value);
        return (nodeAdded != null);
    }

    /**
     * Ajouter une valeur à l'arbre et retourner le nœud qui a été ajouté.
     * L'arbre peut contenir plusieurs valeurs égales.
     * 
     * @param value
     *            T to add to the tree.
     * @return Node<T> which was added to the tree.
     */
    protected Node<T> addValue(T value) {
        Node<T> newNode = this.creator.createNewNode(null, value);

        // If root is null, assign
        if (root == null) {
            root = newNode;
            size++;
            return newNode;
        }

        Node<T> node = root;
        while (node != null) {
            if (newNode.id.compareTo(node.id) <= 0) {
                // Less than or equal to goes left
                if (node.lesser == null) {
                    // New left node
                    node.lesser = newNode;
                    newNode.parent = node;
                    size++;
                    return newNode;
                }
                node = node.lesser;
            } else {
                // Greater than goes right
                if (node.greater == null) {
                    // New right node
                    node.greater = newNode;
                    newNode.parent = node;
                    size++;
                    return newNode;
                }
                node = node.greater;
            }
        }

        return newNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(T value) {
        Node<T> node = getNode(value);
        return (node != null);
    }

    /**
     * Localisez T dans l'arbre.
     * 
     * @param value
     * @return Node<T> représentant la première référence de la valeur
     * dans l'arbre ou NULL si non trouvé.
     */
    protected Node<T> getNode(T value) {
        Node<T> node = root;
        while (node != null && node.id != null) {
            if (value.compareTo(node.id) < 0) {
                node = node.lesser;
            } else if (value.compareTo(node.id) > 0) {
                node = node.greater;
            } else if (value.compareTo(node.id) == 0) {
                return node;
            }
        }
        return null;
    }

    /**
     * Faire pivoter l'arbre à gauche au niveau du sous-arbre enraciné au nœud.
     * 
     * @param node
     *            Racine de l'arbre à tourner à gauche.
     */
    protected void rotateLeft(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> greater = node.greater;
        Node<T> lesser = greater.lesser;

        greater.lesser = node;
        node.parent = greater;

        node.greater = lesser;

        if (lesser != null)
            lesser.parent = node;

        if (parent!=null) {
            if (node == parent.lesser) {
                parent.lesser = greater;
            } else if (node == parent.greater) {
                parent.greater = greater;
            } else {
                throw new RuntimeException("Yikes! I'm not related to my parent. " + node.toString());
            }
            greater.parent = parent;
        } else {
            root = greater;
            root.parent = null;
        }
    }

    /**
     * Faire pivoter l'arbre à droite au niveau du sous-arbre enraciné au nœud.
     * 
     * @param node
     *            Racine de l'arbre à tourner à droite.
     */
    protected void rotateRight(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> lesser = node.lesser;
        Node<T> greater = lesser.greater;

        lesser.greater = node;
        node.parent = lesser;

        node.lesser = greater;

        if (greater != null)
            greater.parent = node;

        if (parent!=null) {
            if (node == parent.lesser) {
                parent.lesser = lesser;
            } else if (node == parent.greater) {
                parent.greater = lesser;
            } else {
                throw new RuntimeException("Yikes! I'm not related to my parent. " + node.toString());
            }
            lesser.parent = parent;
        } else {
            root = lesser;
            root.parent = null;
        }
    }

    /**
     * la fonction permet d'obtenir le plus grand noeud dans la sous-arborescence enracinée
     * au StartingNode. La recherche n'inclut pas le StartingNode dans ses résultats.
     * 
     * @param startingNode
     *            Root of tree to search.
     * @return Node<T> qui représente le plus grand nœud dans la sous-arborescence startingNode
     * ou NULL si startingNode n'a pas de plus grand enfant.
     */
    protected Node<T> getGreatest(Node<T> startingNode) {
        if (startingNode == null)
            return null;

        Node<T> greater = startingNode.greater;
        while (greater != null && greater.id != null) {
            Node<T> node = greater.greater;
            if (node != null && node.id != null)
                greater = node;
            else
                break;
        }
        return greater;
    }

    /**
     * la fonction permet d'obtenir le moins de noeud dans la sous-arborescence enraciné
     * au startingNode. La recherche n'inclut pas le startingNode dans ses résultats.
     * 
     * @param startingNode
     *            Root of tree to search.
     * @return Node<T> qui représente le plus petit noeud dans la sous-arborescence du startingNode
     * ou NULL si le startingNode n'a pas de plus petits enfants.
     */
    protected Node<T> getLeast(Node<T> startingNode) {
        if (startingNode == null)
            return null;

        Node<T> lesser = startingNode.lesser;
        while (lesser != null && lesser.id != null) {
            Node<T> node = lesser.lesser;
            if (node != null && node.id != null)
                lesser = node;
            else
                break;
        }
        return lesser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T remove(T value) {
        Node<T> nodeToRemove = this.removeValue(value);
        return ((nodeToRemove!=null)?nodeToRemove.id:null);
    }

    /**
     * Supprimer la première occurrence de la valeur dans l'arbre.
     * 
     * @param value
     *            T to remove from the tree.
     * @return Node<T> qui a été retiré de l'arbre.
     */
    protected Node<T> removeValue(T value) {
        Node<T> nodeToRemoved = this.getNode(value);
        if (nodeToRemoved != null) 
            nodeToRemoved = removeNode(nodeToRemoved);
        return nodeToRemoved;
    }

    /**
     * Retirez le nœud à l'aide d'un remplacement
     * 
     * @param nodeToRemoved
     *            Node<T> à retirer de l'arbre.
     * @return nodeRemove
     *            Node<T> retiré de l'arbre,
     *            il peut être différent du paramètre dans certains cas.
     */
    protected Node<T> removeNode(Node<T> nodeToRemoved) {
        if (nodeToRemoved != null) {
            Node<T> replacementNode = this.getReplacementNode(nodeToRemoved);
            replaceNodeWithNode(nodeToRemoved, replacementNode);
        }
        return nodeToRemoved;
    }

    /**
     * la fonction permet d'obtenir le bon nœud de remplacement selon
     * l'algorithme de l'arbre de recherche binaire de l'arbre.
     * 
     * @param nodeToRemoved
     *            Node<T> pour trouver un remplaçant.
     * @return Node<T> qui peut être utilisé pour remplacer nodeToRemoved.
     * nodeToRemoved ne doit PAS être NULL.
     */
    protected Node<T> getReplacementNode(Node<T> nodeToRemoved) {
        Node<T> replacement = null;
        if (nodeToRemoved.greater != null && nodeToRemoved.lesser != null) {
            // Deux enfants.
            // Ajoutez un peu d'aléatoire aux suppressions,
            // ainsi nous n'utilisons pas toujours le plus grand/le
            // moins grand sur la suppression
            if (modifications % 2 != 0) {
                replacement = this.getGreatest(nodeToRemoved.lesser);
                if (replacement == null)
                    replacement = nodeToRemoved.lesser;
            } else {
                replacement = this.getLeast(nodeToRemoved.greater);
                if (replacement == null)
                    replacement = nodeToRemoved.greater;
            }
            modifications++;
        } else if (nodeToRemoved.lesser != null && nodeToRemoved.greater == null) {
            replacement = nodeToRemoved.lesser;
        } else if (nodeToRemoved.greater != null && nodeToRemoved.lesser == null) {
            replacement = nodeToRemoved.greater;
        }
        return replacement;
    }

    /**
     * La fonction remplace nodeToRemoved par replacementNode dans l'arborescence.
     * 
     * @param nodeToRemoved
     *
     * @param replacementNode
     */
    protected void replaceNodeWithNode(Node<T> nodeToRemoved, Node<T> replacementNode) {
        if (replacementNode != null) {
            // Sauvegarde pour plus tard
            Node<T> replacementNodeLesser = replacementNode.lesser;
            Node<T> replacementNodeGreater = replacementNode.greater;

            // Remplace les branches de remplacementNode par les branches de remplacementToRemove
            Node<T> nodeToRemoveLesser = nodeToRemoved.lesser;
            if (nodeToRemoveLesser != null && nodeToRemoveLesser != replacementNode) {
                replacementNode.lesser = nodeToRemoveLesser;
                nodeToRemoveLesser.parent = replacementNode;
            }
            Node<T> nodeToRemoveGreater = nodeToRemoved.greater;
            if (nodeToRemoveGreater != null && nodeToRemoveGreater != replacementNode) {
                replacementNode.greater = nodeToRemoveGreater;
                nodeToRemoveGreater.parent = replacementNode;
            }

            // Supprime le lien du parent du nœud de remplacement vers le remplacement
            Node<T> replacementParent = replacementNode.parent;
            if (replacementParent != null && replacementParent != nodeToRemoved) {
                Node<T> replacementParentLesser = replacementParent.lesser;
                Node<T> replacementParentGreater = replacementParent.greater;
                if (replacementParentLesser != null && replacementParentLesser == replacementNode) {
                    replacementParent.lesser = replacementNodeGreater;
                    if (replacementNodeGreater != null)
                        replacementNodeGreater.parent = replacementParent;
                } else if (replacementParentGreater != null && replacementParentGreater == replacementNode) {
                    replacementParent.greater = replacementNodeLesser;
                    if (replacementNodeLesser != null)
                        replacementNodeLesser.parent = replacementParent;
                }
            }
        }

        // Met à jour le lien dans l'arborescence du nœudToRemoved vers le nœud replacementNode
        Node<T> parent = nodeToRemoved.parent;
        if (parent == null) {
            // Remplacement du nœud racine
            root = replacementNode;
            if (root != null)
                root.parent = null;
        } else if (parent.lesser != null && (parent.lesser.id.compareTo(nodeToRemoved.id) == 0)) {
            parent.lesser = replacementNode;
            if (replacementNode != null)
                replacementNode.parent = parent;
        } else if (parent.greater != null && (parent.greater.id.compareTo(nodeToRemoved.id) == 0)) {
            parent.greater = replacementNode;
            if (replacementNode != null)
                replacementNode.parent = parent;
        }
        size--;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        if (root == null) return true;
        return validateNode(root);
    }

    /**
     * fonction de validation de nœud pour tous les invariants de l'arbre de recherche binaire.
     * 
     * @param node
     */
    protected boolean validateNode(Node<T> node) {
        Node<T> lesser = node.lesser;
        Node<T> greater = node.greater;

        boolean lesserCheck = true;
        if (lesser != null && lesser.id != null) {
            lesserCheck = (lesser.id.compareTo(node.id) <= 0);
            if (lesserCheck)
                lesserCheck = validateNode(lesser);
        }
        if (!lesserCheck)
            return false;

        boolean greaterCheck = true;
        if (greater != null && greater.id != null) {
            greaterCheck = (greater.id.compareTo(node.id) > 0);
            if (greaterCheck)
                greaterCheck = validateNode(greater);
        }
        return greaterCheck;
    }

    /**
     * représentation matricielle de l'arbre dans le premier ordre de recherche.
     */
    public T[] getBFS() { 
        return getBFS(this.root, this.size);
    }

    /**
     * représentation matricielle de l'arbre dans le premier ordre de recherche.
     * 
     * @param start noeud racine
     * @param size d'arbre enraciné au départ
     *
     */
    public static <T extends Comparable<T>> T[] getBFS(Node<T> start, int size) {
        final Queue<Node<T>> queue = new ArrayDeque<Node<T>>();
        final T[] values = (T[])Array.newInstance(start.id.getClass(), size);
        int count = 0;
        Node<T> node = start;
        while (node != null) {
            values[count++] = node.id;
            if (node.lesser != null)
                queue.add(node.lesser);
            if (node.greater != null)
                queue.add(node.greater);
            if (!queue.isEmpty())
                node = queue.remove();
            else
                node = null;
        }
        return values;
    }

    /**
     * représentation en tableau de l'arbre dans l'ordre des niveaux.
     */
    public T[] getLevelOrder() {
        return getBFS();
    }

    /**
     * représentation en tableau de l'arbre dans l'ordre.
     * 
     * @param order ordre de recherche
     *
     */
    public T[] getDFS(DepthFirstSearchOrder order) {
        return getDFS(order, this.root, this.size);
    }

    /**
     * Obtenez une représentation en tableau de l'arbre dans l'ordre.
     * 
     * @param order ordre de recherche
     * @param start noeud racine
     * @param size d'arbre enraciné au départ
     *
     */
    public static <T extends Comparable<T>> T[] getDFS(DepthFirstSearchOrder order, Node<T> start, int size) {
        final Set<Node<T>> added = new HashSet<Node<T>>(2);
        final T[] nodes = (T[])Array.newInstance(start.id.getClass(), size);
        int index = 0;
        Node<T> node = start;
        while (index < size && node != null) {
            Node<T> parent = node.parent;
            Node<T> lesser = (node.lesser != null && !added.contains(node.lesser)) ? node.lesser : null;
            Node<T> greater = (node.greater != null && !added.contains(node.greater)) ? node.greater : null;

            if (parent == null && lesser == null && greater == null) {
                if (!added.contains(node))
                    nodes[index++] = node.id;
                break;
            }

            if (order == DepthFirstSearchOrder.inOrder) {
                if (lesser != null) {
                    node = lesser;
                } else {
                    if (!added.contains(node)) {
                        nodes[index++] = node.id;
                        added.add(node);
                    }
                    if (greater != null) {
                        node = greater;
                    } else if (added.contains(node)) {
                        node = parent;
                    } else {
                        // We should not get here. Stop the loop!
                        node = null;
                    }
                }
            } else if (order == DepthFirstSearchOrder.preOrder) {
                if (!added.contains(node)) {
                    nodes[index++] = node.id;
                    added.add(node);
                }
                if (lesser != null) {
                    node = lesser;
                } else if (greater != null) {
                    node = greater;
                } else if (added.contains(node)) {
                    node = parent;
                } else {
                    // We should not get here. Stop the loop!
                    node = null;
                }
            } else {
                // post-Order
                if (lesser != null) {
                    node = lesser;
                } else {
                    if (greater != null) {
                        node = greater;
                    } else {
                        // lesser==null && greater==null
                        nodes[index++] = node.id;
                        added.add(node);
                        node = parent;
                    }
                }
            }
        }
        return nodes;
    }

    /**
     * Obtenez une représentation en tableau de l'arbre dans un ordre trié.
     *
     */
    public T[] getSorted() {
        // Depth first search to traverse the tree in-order sorted.
        return getDFS(DepthFirstSearchOrder.inOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public java.util.Collection<T> toCollection() {
        return (new JavaCompatibleBinarySearchTree<T>(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return TreePrinter.getString(this);
    }

    protected static class Node<T extends Comparable<T>> {

        protected T id = null;
        protected Node<T> parent = null;
        protected Node<T> lesser = null;
        protected Node<T> greater = null;

        /**
         * Node constructor.
         * 
         * @param parent
         *            Parent link in tree. parent can be NULL.
         * @param id
         *            T representing the node in the tree.
         */
        protected Node(Node<T> parent, T id) {
            this.parent = parent;
            this.id = id;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "id=" + id + " parent=" + ((parent != null) ? parent.id : "NULL") + " lesser="
                    + ((lesser != null) ? lesser.id : "NULL") + " greater=" + ((greater != null) ? greater.id : "NULL");
        }
    }

    protected static interface INodeCreator<T extends Comparable<T>> {

        /**
         * Create a new Node with the following parameters.
         * 
         * @param parent
         *            of this node.
         * @param id
         *            of this node.
         * @return new Node
         */
        public Node<T> createNewNode(Node<T> parent, T id);
    }

    protected static class TreePrinter {

        public static <T extends Comparable<T>> String getString(BinarySearchTree<T> tree) {
            if (tree.root == null)
                return "Tree has no nodes.";
            return getString(tree.root, "", true);
        }

        private static <T extends Comparable<T>> String getString(Node<T> node, String prefix, boolean isTail) {
            StringBuilder builder = new StringBuilder();

            if (node.parent != null) {
                String side = "left";
                if (node.equals(node.parent.greater))
                    side = "right";
                builder.append(prefix + (isTail ? "└── " : "├── ") + "(" + side + ") " + node.id + "\n");
            } else {
                builder.append(prefix + (isTail ? "└── " : "├── ") + node.id + "\n");
            }
            List<Node<T>> children = null;
            if (node.lesser != null || node.greater != null) {
                children = new ArrayList<Node<T>>(2);
                if (node.lesser != null)
                    children.add(node.lesser);
                if (node.greater != null)
                    children.add(node.greater);
            }
            if (children != null) {
                for (int i = 0; i < children.size() - 1; i++) {
                    builder.append(getString(children.get(i), prefix + (isTail ? "    " : "│   "), false));
                }
                if (children.size() >= 1) {
                    builder.append(getString(children.get(children.size() - 1), prefix + (isTail ? "    " : "│   "), true));
                }
            }

            return builder.toString();
        }
    }

    private static class JavaCompatibleBinarySearchTree<T extends Comparable<T>> extends java.util.AbstractCollection<T> {

        protected BinarySearchTree<T> tree = null;

        public JavaCompatibleBinarySearchTree(BinarySearchTree<T> tree) {
            this.tree = tree;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean add(T value) {
            return tree.add(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean remove(Object value) {
            return (tree.remove((T)value)!=null);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean contains(Object value) {
            return tree.contains((T)value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int size() {
            return tree.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public java.util.Iterator<T> iterator() {
            return (new BinarySearchTreeIterator<T>(this.tree));
        }

        private static class BinarySearchTreeIterator<C extends Comparable<C>> implements java.util.Iterator<C> {

            private BinarySearchTree<C> tree = null;
            private BinarySearchTree.Node<C> last = null;
            private Deque<BinarySearchTree.Node<C>> toVisit = new ArrayDeque<BinarySearchTree.Node<C>>();

            protected BinarySearchTreeIterator(BinarySearchTree<C> tree) {
                this.tree = tree;
                if (tree.root!=null) toVisit.add(tree.root);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean hasNext() {
                if (toVisit.size()>0) return true; 
                return false;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public C next() {
                while (toVisit.size()>0) {
                    // Go thru the current nodes
                    BinarySearchTree.Node<C> n = toVisit.pop();

                    // Add non-null children
                    if (n.lesser!=null) toVisit.add(n.lesser);
                    if (n.greater!=null) toVisit.add(n.greater);

                    // Update last node (used in remove method)
                    last = n;
                    return n.id;
                }
                return null;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public void remove() {
                tree.removeNode(last);
            }
        }
    }
}
