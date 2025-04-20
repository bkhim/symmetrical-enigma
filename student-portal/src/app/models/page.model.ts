export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  sort: any;
  number: number;
  first: boolean;
  last: boolean;
}
