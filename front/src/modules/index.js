import { combineReducers } from "redux";
import categoryReducer from "./categoryReducer";
import jangtariReducer from "./jangtariReducer";

const rootReducer = combineReducers({
  categoryReducer,
  jangtariReducer,
});

export default rootReducer;
