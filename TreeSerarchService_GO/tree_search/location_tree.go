package tree_search

import (
	"fmt"
	"go_tree_service/utils"
	"strconv"
)

func CreateLocationTree(node *utils.Node, baseMatrix map[int64]map[int64]float32) *LocationNode {
	root := &LocationNode{
		ID:       1,
		Name:     "root",
		Costs:    baseMatrix[1],
		Children: []*LocationNode{},
	}

	CreateLocationRecursive(node, root, baseMatrix, 2)
	IndexLocationElements(root)
	return root
}

func IndexLocationElements(root *LocationNode) int64 {
	if root == nil {
		return 0
	}
	maxID := root.ID
	for i := 0; i < len(root.Children); i++ {
		childMaxID := IndexLocationElements(root.Children[i])
		if childMaxID > maxID {
			maxID = childMaxID
		}
	}
	root.MaxBrunchID = maxID
	return maxID

}

func CreateLocationRecursive(node *utils.Node, currentNode *LocationNode, matrix map[int64]map[int64]float32, startID int64) int64 {
	if node == nil || node.Children == nil {
		return startID
	}
	for _, child := range node.Children {
		newNode := &LocationNode{
			ID:       startID,
			Name:     child.Name,
			Costs:    matrix[int64(startID)],
			Parent:   currentNode,
			Children: []*LocationNode{},
		}
		currentNode.Children = append(currentNode.Children, newNode)
		startID = CreateLocationRecursive(child, newNode, matrix, startID+1)
	}
	return startID
}

func AddLocation(patentID int64, name string) {
	AddLocationRecursive(patentID, name, GetTree().LocationTree)
	IndexLocationElements(GetTree().LocationTree)
}

func AddLocationRecursive(patentID int64, name string, currentNode *LocationNode) {
	if currentNode.ID == patentID {
		if currentNode.Children != nil {
			for i := 0; i < len(currentNode.Children); i++ {
				AddLocationRecursive(patentID, name, currentNode.Children[i])
			}
		}
		if (currentNode.Children) == nil {
			currentNode.Children = []*LocationNode{}
		}
		currentNode.Children = append(currentNode.Children, &LocationNode{
			ID:       currentNode.ID + 1,
			Name:     name,
			Parent:   currentNode,
			Costs:    map[int64]float32{},
			Children: []*LocationNode{},
		})
	} else {
		if currentNode.ID >= patentID {
			currentNode.ID = currentNode.ID + 1
		}
		if currentNode.Children == nil {
			return
		}
		for i := 0; i < len(currentNode.Children); i++ {
			if (currentNode.Children[i].MaxBrunchID) >= patentID {
				AddLocationRecursive(patentID, name, currentNode.Children[i])
			}
		}
	}
}

func FindLocationNodeByID(id int64) *LocationNode {
	return FindLocationNodeByIDRecursive(id, GetTree().LocationTree)
}

func FindLocationNodeByIDRecursive(id int64, currentNode *LocationNode) *LocationNode {
	if currentNode.ID == id {
		return currentNode
	}
	if (currentNode.Children) != nil {
		for i := 0; i < len(currentNode.Children); i++ {
			if currentNode.Children[i].MaxBrunchID >= id {
				return FindLocationNodeByIDRecursive(id, currentNode.Children[i])
			}
		}
	}
	return nil
}

func PrintLocationTree(tree *LocationNode, i int) {
	for _, child := range tree.Children {
		for j := 0; j < i; j++ {
			fmt.Print("    ")
		}
		fmt.Println(child.Name + ":" + strconv.Itoa(int(child.ID)))
		PrintLocationTree(child, i+1)
	}
}

// Откуда брать матрицы
// Как лучше организовать подгрузку матриц
// Нужно ли менять значения в матрицах
