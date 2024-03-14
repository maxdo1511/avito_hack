package api

import "go_tree_service/tree_search"

type GetCostRequest struct {
	UserID     int64
	LocationId int64
	CategoryId int64
}

func GetUserCost(Request GetCostRequest) float32 {
	return tree_search.FindCostByLocationAndCategory(Request.UserID, Request.LocationId, Request.CategoryId)
}
