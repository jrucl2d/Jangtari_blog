import { combineReducers } from "redux";
import categoryReducer from "./categoryReducer";
import jangtariReducer from "./jangtariReducer";
import postReducer from "./postReducer";

const rootReducer = combineReducers({
  categoryReducer,
  jangtariReducer,
  postReducer,
});

export default rootReducer;
