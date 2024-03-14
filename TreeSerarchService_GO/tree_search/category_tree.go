package tree_search

import (
	"fmt"
	"go_tree_service/utils"
	"strconv"
)

func CreateCategoryTree(node *utils.Node) *CategoryNode {
	if node == nil {
		return nil
	}
	root := &CategoryNode{
		ID:       1,
		Name:     "root",
		Children: []*CategoryNode{},
	}
	CrateCategoryRecursive(node, root, 2)
	IndexCategoryElements(root)
	return root
}

func IndexCategoryElements(root *CategoryNode) int64 {
	if root == nil {
		return 0
	}
	maxID := root.ID
	for i := 0; i < len(root.Children); i++ {
		childMaxID := IndexCategoryElements(root.Children[i])
		if childMaxID > maxID {
			maxID = childMaxID
		}
	}
	root.MaxBrunchID = maxID
	return maxID
}

func CrateCategoryRecursive(node *utils.Node, currentNode *CategoryNode, startID int64) int64 {
	if node == nil || node.Children == nil {
		return startID
	}
	for _, child := range node.Children {
		newNode := &CategoryNode{
			ID:       startID,
			Name:     child.Name,
			Parent:   currentNode,
			Children: []*CategoryNode{},
		}
		currentNode.Children = append(currentNode.Children, newNode)
		startID = CrateCategoryRecursive(child, newNode, startID+1)
	}
	return startID
}

func AddCategory(patentID int64, name string) {
	AddCategoryRecursive(patentID, name, GetTree().CategoryTree)
	IndexCategoryElements(GetTree().CategoryTree)
}

func AddCategoryRecursive(patentID int64, name string, currentNode *CategoryNode) {
	if currentNode.ID == patentID {
		if currentNode.Children != nil {
			for i := 0; i < len(currentNode.Children); i++ {
				AddCategoryRecursive(patentID, name, currentNode.Children[i])
			}
		}
		if (currentNode.Children) == nil {
			currentNode.Children = []*CategoryNode{}
		}
		currentNode.Children = append(currentNode.Children, &CategoryNode{
			ID:       currentNode.ID + 1,
			Name:     name,
			Parent:   currentNode,
			Children: []*CategoryNode{},
		})
	} else {
		if currentNode.ID >= patentID {
			currentNode.ID = currentNode.ID + 1
		}
		if currentNode.Children == nil {
			return
		}
		for i := 0; i < len(currentNode.Children); i++ {
			if currentNode.Children[i].MaxBrunchID >= patentID {
				AddCategoryRecursive(patentID, name, currentNode.Children[i])
			}
		}
	}
}

func FindCategoryNodeByID(id int64) *CategoryNode {
	return FindCategoryNodeByIDRecursive(id, GetTree().CategoryTree)
}

func FindCategoryNodeByIDRecursive(id int64, tree *CategoryNode) *CategoryNode {
	if tree.ID == id {
		return tree
	}
	if tree.Children != nil {
		for i := 0; i < len(tree.Children); i++ {
			if tree.Children[i].MaxBrunchID >= id {
				return FindCategoryNodeByIDRecursive(id, tree.Children[i])
			}
		}
	}
	return nil
}

func PrintCategoryTree(tree *CategoryNode, i int) {
	for _, child := range tree.Children {
		for j := 0; j < i; j++ {
			fmt.Print("    ")
		}
		fmt.Println(child.Name + ":" + strconv.Itoa(int(child.ID)))
		PrintCategoryTree(child, i+1)
	}
}
