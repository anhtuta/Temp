import React, { PureComponent } from 'react';
import Moment from 'react-moment';
import Table from '../../components/Table/Table';
import SearchBox from '../../components/Input/SearchBox';
import Button from '../../components/Button/Button';
import { ACTION_ADD, ACTION_EDIT } from '../../constants/Constants';
import BookUpsertModal from './BookUpsertModal';
import Toast from '../../components/Toast/Toast';
import BookService from './BookService';
import CategoryService from '../Category/CategoryService';
import './Book.scss';
import ConfirmModal from '../../components/Modal/ConfirmModal';

class Book extends PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      bookData: {},
      params: {
        page: 0
      },
      loading: false,
      action: '',
      showUpsertModal: false,
      showConfirmModal: false,
      categoryOptions: [],
      selectedRow: {}
    };
  }

  componentDidMount() {
    // get all category for creating new or updating book
    CategoryService.getAllCategories()
      .then((res) => {
        const categoryOptions = res.data.map((item) => ({
          value: item.id,
          label: item.name
        }));
        this.setState({ categoryOptions });
      })
      .catch((err) => {
        console.log(err);
        this.setState({
          loading: false
        });
      });
  }

  getBooks = (params) => {
    this.setState({ loading: true });
    const sort = params.sortBy
      ? params.sortBy + ',' + params.sortOrder
      : this.state.params.sort;
    const newParams = {
      ...this.state.params,
      ...params,
      sort
    };
    this.setState({
      params: newParams
    });
    BookService.getBooks(newParams)
      .then((res) => {
        this.setState({
          bookData: res.data,
          totalCount: res.meta,
          loading: false
        });
      })
      .catch((err) => {
        console.log(err);
        this.setState({
          loading: false
        });
      });
  };

  getBooks2 = async (params) => {
    this.setState({ loading: true });
    const newParams = { ...this.state.params, ...params };
    this.setState({
      params: newParams
    });
    try {
      const res = await BookService.getBooks(newParams);
      this.setState({
        bookData: res.data,
        totalCount: res.meta,
        loading: false
      });
    } catch (err) {
      console.log(err);
      this.setState({
        loading: false
      });
    }
  };

  columns = [
    {
      Header: 'Title',
      accessor: 'title'
    },
    {
      Header: 'Author',
      accessor: 'author'
    },
    {
      Header: 'Category',
      accessor: 'categoryName',
      Cell: ({ original }) => original.category.name,
      sortable: false
    },
    {
      Header: 'Price',
      accessor: 'price'
    },
    {
      Header: 'Created date',
      accessor: 'createdDate',
      Cell: ({ original }) => (
        <Moment format="HH:mm DD/MM/YYYY">{original.createdDate}</Moment>
      )
    },
    {
      Header: 'Modified date',
      accessor: 'modifiedDate',
      Cell: ({ original }) => (
        <Moment format="HH:mm DD/MM/YYYY">{original.modifiedDate}</Moment>
      )
    },
    {
      Header: 'Action',
      Cell: ({ original }) => (
        <div>
          <i
            className="fas fa-edit icon-btn-action icon-btn-edit"
            onClick={() => this.onUpdate(original)}
          ></i>
          &nbsp;
          <i
            className="fas fa-trash-alt icon-btn-action icon-btn-delete"
            onClick={() => this.onDelete(original)}
          ></i>
        </div>
      ),
      width: 80
    }
  ];

  onSearch = (obj) => {
    this.getBooks({ searchText: obj.value });
  };

  onAddNew = () => {
    this.setState({
      action: ACTION_ADD,
      showUpsertModal: true
    });
  };

  getCategoryOptionById = (id) => {
    return this.state.categoryOptions.find((option) => {
      return option.value === id;
    });
  };

  onUpdate = (original) => {
    console.log('original: ', original);
    const selectedRow = {
      id: original.id,
      title: original.title,
      author: original.author,
      category: this.getCategoryOptionById(original.category.id),
      price: original.price
    };
    this.setState({
      action: ACTION_EDIT,
      showUpsertModal: true,
      selectedRow
    });
  };

  onDelete = (original) => {
    this.setState({
      showConfirmModal: true,
      selectedRow: { id: original.id, title: original.title }
    });
  };

  onCloseUpsertModal = () => {
    this.setState({
      showUpsertModal: false,
      selectedRow: {}
    });
  };

  onCloseConfirmModal = () => {
    this.setState({
      showConfirmModal: false,
      selectedRow: {}
    });
  };

  onSave = () => {
    this.onCloseUpsertModal();
    this.getBooks(this.state.params);
  };

  onDeleteBook = () => {
    BookService.deleteBook(this.state.selectedRow.id)
    .then((res) => {
      Toast.success(res.message);
      this.onCloseConfirmModal();
      this.getBooks(this.state.params);
    })
    .catch((err) => {
      console.log(err);
      Toast.error(err);
    });
  };

  render() {
    const {
      bookData,
      loading,
      showUpsertModal,
      showConfirmModal,
      categoryOptions,
      action,
      selectedRow
    } = this.state;

    return (
      <div className="book-wrapper">
        <h2>All book</h2>
        <div className="search-section">
          <div className="width25">
            <SearchBox name="searchText" onSearch={this.onSearch} />
          </div>

          <Button
            text="Add new"
            className="btn-success btn-add-new"
            onClick={this.onAddNew}
          />
        </div>

        <Table
          columns={this.columns}
          data={bookData}
          loading={loading}
          onFetchData={this.getBooks}
          className="book-table"
          defaultPageSize={10}
        />

        {showUpsertModal && (
          <BookUpsertModal
            showUpsertModal={showUpsertModal}
            onCloseUpsertModal={this.onCloseUpsertModal}
            categoryOptions={categoryOptions}
            selectedRow={selectedRow}
            action={action}
            onSave={this.onSave}
          />
        )}
        {showConfirmModal && (
          <ConfirmModal
            show={showConfirmModal}
            modalTitle={`Delete this book "${selectedRow.title}", this cannot be undone?`}
            saveButtonText="Delete"
            cancelButtonText="Cancel"
            isDelete={true}
            onSave={this.onDeleteBook}
            onClose={this.onCloseConfirmModal}
            onCancel={this.onCloseConfirmModal}
          />
        )}
      </div>
    );
  }
}

export default Book;
