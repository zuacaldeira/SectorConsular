import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ApiService } from './api.service';

describe('ApiService', () => {
  let service: ApiService;
  let httpMock: HttpTestingController;
  const baseUrl = 'http://localhost:8090/api/v1';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ApiService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('get', () => {
    it('should perform GET request with correct URL and no params', () => {
      const mockData = { id: 1, name: 'test' };

      service.get('/sprints').subscribe(data => {
        expect(data).toEqual(mockData);
      });

      const req = httpMock.expectOne(`${baseUrl}/sprints`);
      expect(req.request.method).toBe('GET');
      expect(req.request.params.keys().length).toBe(0);
      req.flush(mockData);
    });

    it('should perform GET request with query params', () => {
      const mockData = [{ id: 1 }];

      service.get('/tasks', { page: 0, size: 20, status: 'ACTIVE' }).subscribe(data => {
        expect(data).toEqual(mockData);
      });

      const req = httpMock.expectOne(r =>
        r.url === `${baseUrl}/tasks` &&
        r.params.get('page') === '0' &&
        r.params.get('size') === '20' &&
        r.params.get('status') === 'ACTIVE'
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockData);
    });

    it('should filter out null and undefined param values', () => {
      service.get('/calendar', { year: 2026, month: null, day: undefined }).subscribe();

      const req = httpMock.expectOne(r =>
        r.url === `${baseUrl}/calendar` &&
        r.params.get('year') === '2026' &&
        !r.params.has('month') &&
        !r.params.has('day')
      );
      expect(req.request.method).toBe('GET');
      expect(req.request.params.keys().length).toBe(1);
      req.flush({});
    });

    it('should handle empty params object', () => {
      service.get('/test', {}).subscribe();

      const req = httpMock.expectOne(`${baseUrl}/test`);
      expect(req.request.method).toBe('GET');
      expect(req.request.params.keys().length).toBe(0);
      req.flush({});
    });

    it('should convert numeric param values to strings', () => {
      service.get('/tasks', { sprintId: 3 }).subscribe();

      const req = httpMock.expectOne(r =>
        r.url === `${baseUrl}/tasks` &&
        r.params.get('sprintId') === '3'
      );
      expect(req.request.method).toBe('GET');
      req.flush({});
    });
  });

  describe('post', () => {
    it('should perform POST request with body', () => {
      const body = { username: 'admin', password: 'admin123' };
      const mockResponse = { token: 'abc', role: 'DEVELOPER' };

      service.post('/auth/login', body).subscribe(data => {
        expect(data).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${baseUrl}/auth/login`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(body);
      req.flush(mockResponse);
    });

    it('should perform POST request without body', () => {
      const mockResponse = { id: 1, status: 'IN_PROGRESS' };

      service.post('/tasks/1/start').subscribe(data => {
        expect(data).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${baseUrl}/tasks/1/start`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toBeNull();
      req.flush(mockResponse);
    });
  });

  describe('patch', () => {
    it('should perform PATCH request with body', () => {
      const body = { status: 'COMPLETED', actualHours: 3.5 };
      const mockResponse = { id: 5, ...body };

      service.patch('/sprints/5', body).subscribe(data => {
        expect(data).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${baseUrl}/sprints/5`);
      expect(req.request.method).toBe('PATCH');
      expect(req.request.body).toEqual(body);
      req.flush(mockResponse);
    });
  });

  describe('put', () => {
    it('should perform PUT request with body', () => {
      const body = { name: 'Updated Sprint', weeks: 4 };
      const mockResponse = { id: 1, ...body };

      service.put('/sprints/1', body).subscribe(data => {
        expect(data).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${baseUrl}/sprints/1`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(body);
      req.flush(mockResponse);
    });
  });

  describe('delete', () => {
    it('should perform DELETE request', () => {
      service.delete('/tasks/10/notes/3').subscribe();

      const req = httpMock.expectOne(`${baseUrl}/tasks/10/notes/3`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });

  describe('URL construction', () => {
    it('should prepend baseUrl to all paths', () => {
      service.get('/dashboard').subscribe();
      const req = httpMock.expectOne(`${baseUrl}/dashboard`);
      expect(req.request.url).toBe('http://localhost:8090/api/v1/dashboard');
      req.flush({});
    });

    it('should handle nested paths correctly', () => {
      service.get('/sprints/2/progress').subscribe();
      const req = httpMock.expectOne(`${baseUrl}/sprints/2/progress`);
      expect(req.request.url).toBe('http://localhost:8090/api/v1/sprints/2/progress');
      req.flush({});
    });
  });
});
