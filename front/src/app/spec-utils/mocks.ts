import { of } from "rxjs";

export class MockRouter {
    get url(): string {
        return '';
    }
    navigate(): Promise<boolean> {
        return new Promise<boolean>((resolve, _) => resolve(true));
    }
}

export class MockSnackBar {
    open() {
      return {
        onAction: () => of({})
      }
    }
  }