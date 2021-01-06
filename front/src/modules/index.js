import { combineReducers } from "redux";
import categoryReducer from "./categoryReducer";
import jangtariReducer from "./jangtariReducer";
import postReducer from "./postReducer";
import memberReducer from "./memberReducer";

const rootReducer = combineReducers({
  categoryReducer,
  jangtariReducer,
  postReducer,
  memberReducer,
});

export default rootReducer;
