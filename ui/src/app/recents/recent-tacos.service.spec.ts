import { TestBed } from '@angular/core/testing';

import { RecentTacosService } from './recent-tacos.service';

describe('RecentTacosService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RecentTacosService = TestBed.get(RecentTacosService);
    expect(service).toBeTruthy();
  });
});
