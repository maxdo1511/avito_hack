package api

import "go_tree_service/tree_search"

type GetTreesResponse struct {
	LocationTree *tree_search.LocationNode
	CategoryTree *tree_search.CategoryNode
}

type AddNodeRequest struct {
	Struct string
	Id     int64 // parent id
	Name   string
}

type ChangeCostRequest struct {
	Struct     string
	LocationID int64
	CategoryID int64
	Cost       float32
}

type DeleteNodeRequest struct {
	Struct string
	Id     int64
}

func GetTrees() (t GetTreesResponse) {
	t.LocationTree = tree_search.GetTree().LocationTree
	t.CategoryTree = tree_search.GetTree().CategoryTree
	return t
}

func AddNode(r AddNodeRequest) {
	switch r.Struct {
	case "location":
		tree_search.AddLocation(r.Id, r.Name)
	case "category":
		tree_search.AddCategory(r.Id, r.Name)
	}
}

/*
func ChangeNode(r ChangeNodeRequest) {
	switch r.Struct {
	case "location":
		tree_search.ChangeLocation(r.Id, r.Name)
	case "category":
		tree_search.ChangeCategory(r.Id, r.Name)
	}
}

func DeleteNode(r DeleteNodeRequest) {
	switch r.Struct {
	case "location":
		tree_search.DeleteLocation(r.Id)
	case "category":
		tree_search.DeleteCategory(r.Id)
	}
}

*/
