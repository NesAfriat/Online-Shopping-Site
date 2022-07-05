import {Basket} from "./Basket";

export class Cart {
  [shopId: number]: Basket;
}